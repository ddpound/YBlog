package com.example.yblog.main.controller;


import com.example.yblog.board.service.BoardService;
import com.example.yblog.model.YUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
    @Autowired
    BoardService boardService;

    // 메인
    @GetMapping(value = {"/index" , "/"})
    public  String mainView(Model model , @PageableDefault(size = 3,sort = "id",direction = Sort.Direction.DESC)Pageable pagealbe){

        model.addAttribute("boards", boardService.boardList(pagealbe));
        model.addAttribute("boardsPage", boardService.boardListPage(pagealbe));

        return "index";
    }

    @GetMapping("/auth/patchnote")
    public String patchnote(){
        return "board/patchnote";
    }


}
