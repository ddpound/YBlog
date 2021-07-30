package com.example.yblog.handler;

import javassist.NotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.NoHandlerFoundException;

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

    @ExceptionHandler(value = HttpClientErrorException.BadRequest.class)
    public String handleExceptionBadeRequest(Exception e, Model model){
        System.out.println("Error : "+ e.getMessage());
        model.addAttribute("errorM", "잘못된 요청입니다!(카카오톡 회원가입도중 새로고침을 하거나 잘못된 링크요청시 발생합니다!)");
        return "error/error";
    }


    // 이녀석에 관한건 여기로 전송
    @ExceptionHandler(value = Exception.class)
    public String handleException(Exception e, Model model){
        System.out.println("Error : "+ e.getMessage());
        model.addAttribute("errorM", "에러발생! 문의 남겨주시면 해결하겠습니다!");
        return "error/error";
    }


}
