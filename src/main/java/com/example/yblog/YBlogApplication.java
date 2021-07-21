package com.example.yblog;

import com.example.yblog.allstatic.IpHostName;
import com.example.yblog.model.Status;
import com.example.yblog.status.StatusService;
import org.apache.catalina.connector.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
public class YBlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(YBlogApplication.class, args);
    }

}
