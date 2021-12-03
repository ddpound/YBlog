package com.example.yblog;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class YBlogApplication {

    public static void main(String[] args) {

        SpringApplication.run(YBlogApplication.class, args);
    }

}
