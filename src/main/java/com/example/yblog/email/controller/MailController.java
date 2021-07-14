package com.example.yblog.email.controller;


import com.example.yblog.authkey.StaticAuthKey;
import com.example.yblog.email.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/")
public class MailController {

    @Autowired
    MailService mailService;

    @PostMapping(value = "sendEmail" , produces = "application/json;charset=utf-8")
    @ResponseBody
    public String sendEmail(@RequestBody Map<String,Object> email){
        mailService.mailSend((String) email.get("email"));
        return "{\"result\" :  true}";
    }

    @GetMapping(value = "signUpConfirm")
    public String signUpConfirm(@RequestParam("authKey") String authKey,@RequestParam("email") String email, Model model){
        if(StaticAuthKey.authKey.equals(authKey)){
            System.out.println("인증성공");
            model.addAttribute("authEmail",email);
        }else{
            System.out.println("인증실패");
        }
        return "loginJoin/join";
    }

}
