package com.example.yblog.join.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "join")
public class JoinController {

    @GetMapping(value = "emailauth")
    public String goEmailAuthView(){

        return "loginJoin/emailauth";
    }





}
