package com.example.yblog.handler;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice //모든 에러 발생시 여기로 들어옴
@Controller
public class GlobalExceoptionHandler {

    // 이녀석에 관한건 여기로 전송
    @ExceptionHandler(value = IllegalArgumentException.class)
    public String handleArgumentException(IllegalArgumentException e, Model model){
        model.addAttribute("errorM", e.getMessage());
        return "error/error";
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public String handleException(DataIntegrityViolationException e, Model model){
        model.addAttribute("errorM",  "이미 있는 사용자 입니다");
        return "error/error";
    }

    // 이녀석에 관한건 여기로 전송
    @ExceptionHandler(value = Exception.class)
    public String handleException(Exception e, Model model){
        model.addAttribute("errorM", e.getMessage());
        return "error/error";
    }


}
