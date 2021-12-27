package com.example.yblog;


import com.example.yblog.allstatic.AllStaticElement;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class YBlogApplication {


    public static void main(String[] args) {
        AllStaticElement.ctx = SpringApplication.run(YBlogApplication.class, args);
    }

}
