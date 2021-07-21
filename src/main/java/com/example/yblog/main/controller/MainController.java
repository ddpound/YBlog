package com.example.yblog.main.controller;


import com.example.yblog.allstatic.IpHostName;
import com.example.yblog.board.service.BoardService;
import com.example.yblog.model.Status;
import com.example.yblog.model.YUser;
import com.example.yblog.status.StatusService;
import org.apache.catalina.connector.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Controller
public class MainController {
    @Autowired
    BoardService boardService;

    @Autowired
    StatusService statusService;


    // 메인
    @GetMapping(value = {"/index" , "/"})
    public  String mainView(Model model , @PageableDefault(size = 3,sort = "id",direction = Sort.Direction.DESC)Pageable pagealbe) {

        if(IpHostName.statusNum < 1){
            Status status = new Status();
            status.setNowStatus("저장된 정보가 없습니다");
            statusService.SaveStatus(status);
            IpHostName.statusNum ++;
        }

        model.addAttribute("boards", boardService.boardList(pagealbe));
        model.addAttribute("boardsPage", boardService.boardListPage(pagealbe));
        model.addAttribute("nowStatus" , statusService.statusReturn(1));

        return "index";
    }

    @GetMapping("/auth/patchnote")
    public String patchnote(){
        return "board/patchnote";
    }



}
