package com.example.yblog.login.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "login")
public class LoginController {

    // 로그인창
    @GetMapping(value = "")
    public String loginView(){
        return "loginJoin/login";
    }

}
