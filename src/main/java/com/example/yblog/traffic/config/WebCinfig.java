package com.example.yblog.traffic.config;

import com.example.yblog.allstatic.AllStaticElement;
import com.example.yblog.traffic.trafficinterceptor.HttpInterceptor;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Log4j2
@Configuration
@Component
public class WebCinfig  implements WebMvcConfigurer {

    @Autowired
    HttpInterceptor httpInterceptor;

    // Device 클래스 사용전의 인터셉터 컨테이너 설정이 필요하다
    @Bean
    public DeviceResolverHandlerInterceptor deviceResolverHandlerInterceptor() {
        return new DeviceResolverHandlerInterceptor();
    }



    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(httpInterceptor);
        // Device 설정에 필요한 레지스트리 추가 작업
        registry.addInterceptor(deviceResolverHandlerInterceptor());

    }



    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // OS체크 부분
        String os = System.getProperty("os.name").toLowerCase();
        if(os.contains("win")){
            AllStaticElement.OsName = "window";
            log.info("Now Os Name is window");

        }else{
            AllStaticElement.OsName = "linux";
            log.info("Now Os Name is linux");
        }

        if(AllStaticElement.OsName.equals("window")){
            registry.addResourceHandler("/temporary_storage/**")
                    .addResourceLocations("file:///C:/temporary_storage/");
            // 이렇게 리소스 핸들러 추가도 가능한듯 임시 파일, 업로드시 업로드파일 두개를 만들어야하니 알아두자
            // Confirm_Save (확정 저장 파일 이름)
            registry.addResourceHandler("/Confirm_SaveImage/**")
                    .addResourceLocations("file:///C:/Confirm_SaveImage/");
        }else{
            // 리눅스쪽 애들 경로
            registry.addResourceHandler("/temporary_storage/**")
                    .addResourceLocations("file:///home/youseongjung/Templates/temporary_storage/");
            registry.addResourceHandler("/Confirm_SaveImage/**")
                    .addResourceLocations("file:///home/youseongjung/Templates/Confirm_SaveImage/");
        }

    }


}
