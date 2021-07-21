package com.example.yblog.email.controller;


import com.example.yblog.authkey.StaticAuthKey;
import com.example.yblog.config.SecurityConfig;
import com.example.yblog.email.service.MailService;


import org.apache.catalina.connector.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class MailController {

    @Autowired
    MailService mailService;
    @Autowired
    SecurityConfig securityConfig;

    @PostMapping(value = "/auth/sendEmail" , produces = "application/json;charset=utf-8")
    @ResponseBody
    public String sendEmail(@RequestBody Map<String,Object> email){
        mailService.mailSend((String) email.get("email") );
        return "{\"result\" :  true}";
    }

    @GetMapping(value = "/auth/signUpConfirm")
    public String signUpConfirm(@RequestParam("authKey") String authKey,@RequestParam("email") String email, Model model){
        if(StaticAuthKey.authKey.equals(authKey)){
            System.out.println("Email Authentication Successful :"+ email);
            model.addAttribute("authEmail",email);
        }else{
            System.out.println("Email Authentication Fail");
        }
        return "loginJoin/join";
    }

}
