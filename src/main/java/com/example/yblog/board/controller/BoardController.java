package com.example.yblog.board.controller;


import com.example.yblog.config.auth.PrincipalDetail;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "board")
public class BoardController {

    @GetMapping(value = "write")
    public String goWriteView(@AuthenticationPrincipal PrincipalDetail principal){
        // 이걸로 관리자계정 따로 만들어놔서 따로 통제 만들어놔야겠다
        System.out.println("책 쓰기 접속전 계정 이걸로 따로 또 제약을 둘수 있을꺼같음 : "+ principal.getUsername());
        return "board/write";
    }


}
