package com.example.yblog.traffic.trafficinterceptor;

import com.example.yblog.admin.service.AdminService;
import com.example.yblog.model.BanIp;
import com.example.yblog.traffic.service.PricingPlanService;
import io.github.bucket4j.Bucket;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
@Log4j2
public class HttpInterceptor implements HandlerInterceptor {
    PricingPlanService pricingPlanService = new PricingPlanService();

    @Autowired
   AdminService adminService;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Bucket bucket = pricingPlanService.resolveBucket(request);

        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        log.info("Enter ip address '{}'",ip);

        if(adminService.findIp(ip)){
            System.out.println(ip + " : is BanIp!!!!!");
            return false;
        }

        if (bucket.tryConsume(1)) { // 1개 사용 요청 // 초과하지 않음
            log.info("not over");
            return true;
        } else { // 제한 초과
            log.info("{} traffic over", ip);

            BanIp banIp = new BanIp();
            banIp.setId(ip);
            adminService.saveIp(banIp);

            return false;
        }
    }

    public void postHandle( HttpServletRequest request,
                            HttpServletResponse response, Object handler, ModelAndView modelAndView) {
         }

    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex) {
         }



}
