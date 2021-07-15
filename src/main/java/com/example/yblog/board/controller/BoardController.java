package com.example.yblog.board.controller;


import com.example.yblog.board.service.BoardService;
import com.example.yblog.config.auth.PrincipalDetail;
import com.example.yblog.dto.ResponseDto;
import com.example.yblog.model.YBoard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "board")
public class BoardController {

    @Autowired
    BoardService boardService;

    @GetMapping(value = "write")
    public String goWriteView(@AuthenticationPrincipal PrincipalDetail principal){
        // 이걸로 관리자계정 따로 만들어놔서 따로 통제 만들어놔야겠다
        System.out.println("책 쓰기 접속전 계정 이걸로 따로 또 제약을 둘수 있을꺼같음 : "+ principal.getUsername());
        return "board/write";
    }

    @PostMapping(value = "save")
    @ResponseBody
    public ResponseDto<Integer> boardSave(@RequestBody YBoard yBoard, @AuthenticationPrincipal PrincipalDetail principal){
        boardService.SaveBoard(yBoard, principal.getYUser());

        return new ResponseDto<Integer>(HttpStatus.OK,1);
    }


}
