package com.example.yblog.main.controller;


import com.example.yblog.allstatic.IpHostName;
import com.example.yblog.board.service.BoardService;
import com.example.yblog.model.Status;
import com.example.yblog.portfolio.service.PortfolioBoardService;
import com.example.yblog.status.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MainController {



    @Autowired
    BoardService boardService;

    @Autowired
    StatusService statusService;


    // 메인
    @GetMapping(value = {"/" , "/index"})
    public  String mainView(Model model , @PageableDefault(size = 10, sort = "id",direction = Sort.Direction.DESC)Pageable pagealbe,
                            HttpServletRequest request) {

        // 메인을 불러오면서 딱 한번 하는 부분
        if(IpHostName.statusNum < 1){
            // 현재 상태글 딱 한번  sql 에 저장해야하는 출력해주는 부분
            Status status = new Status();
            status.setNowStatus("저장된 정보가 없습니다");
            statusService.SaveStatus(status);

            //주소값을 바로 받아와 주는 부분 (역시 statusNum 을 통해 딱 한번  업로드 시켜줌)
            System.out.println("Now RequestURL : "+ request.getRequestURL());
            IpHostName.StaticURL = request.getRequestURL().toString();
            String URL =  request.getRequestURL().toString();
            String Localredirect_uri = URL+"auth/kakao/callback";
            String LocalLoginredirect_uri = URL+"auth/kakao/login/callback";
            String LocalLoginRequestURI = "https://kauth.kakao.com/oauth/authorize?"
                    + "client_id="+IpHostName.client_id
                    + "&redirect_uri="+URL+"auth/kakao/callback&response_type=code";

            IpHostName.LoginRequestURI = LocalLoginRequestURI;
            IpHostName.redirect_uri = Localredirect_uri;
            IpHostName.Loginredirect_uri = LocalLoginredirect_uri;
            IpHostName.statusNum ++;
        }

        model.addAttribute("boards", boardService.boardList(pagealbe));
        model.addAttribute("boardsPage", boardService.boardListPage(pagealbe));
        model.addAttribute("nowStatus" , statusService.statusReturn(1));

        return "index";
    }


    @GetMapping(value = "/auth/board/details")
    public String boardDetails(@RequestParam("id") int id , Model model){
        model.addAttribute("board", boardService.boardDetails(id));
        return "board/boardDetails";
    }




}
