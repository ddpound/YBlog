package com.example.yblog.board.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "board")
public class BoardController {

    @GetMapping(value = "write")
    public String goWriteView(){
        return "board/write";
    }


}
