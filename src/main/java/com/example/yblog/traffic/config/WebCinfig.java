package com.example.yblog.traffic.config;

import com.example.yblog.traffic.trafficinterceptor.HttpInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Component
public class WebCinfig  implements WebMvcConfigurer {

    @Autowired
    HttpInterceptor httpInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(httpInterceptor);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/temporary_storage/**")
                .addResourceLocations("file:///C:/temporary_storage/");
        // 이렇게 리소스 핸들러 추가도 가능한듯 임시 파일, 업로드시 업로드파일 두개를 만들어야하니 알아두자
        // Confirm_Save (확정 저장 파일 이름)
        registry.addResourceHandler("/Confirm_SaveImage/**")
                .addResourceLocations("file:///C:/Confirm_SaveImage/");
    }


}
