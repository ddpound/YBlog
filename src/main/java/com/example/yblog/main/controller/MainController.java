package com.example.yblog.main.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "main")
public class MainController {

    // 메인
    @GetMapping(value = "index")
    public  String mainView(){

        return "index";
    }


}
