package com.example.yblog.portfolio.controller;


import com.example.yblog.config.auth.PrincipalDetail;
import com.example.yblog.dto.ResponseDto;
import com.example.yblog.model.PortfolioBoard;
import com.example.yblog.model.YBoard;
import com.example.yblog.portfolio.service.PortfolioBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PortfolioBoardController {

    @Autowired
    PortfolioBoardService portfolioBoardService;

    @GetMapping(value = "/auth/Portfolio")
    public String goPortfolio(Model model, @PageableDefault(sort = "id",
            direction = Sort.Direction.DESC)Pageable pageable){

        model.addAttribute("boards",portfolioBoardService.portboardList(pageable));
        model.addAttribute("boardsPage",portfolioBoardService.portBoardListPage(pageable));
        return "PortfolioBoard/PortfolioBoardDetail";
    }

    @PostMapping(value = "/portboard/save")
    @ResponseBody
    public ResponseDto<Integer> fortboardSave(@RequestBody PortfolioBoard portfolioBoard, @AuthenticationPrincipal PrincipalDetail principal){
        portfolioBoardService.portBoardSave(portfolioBoard,principal);
        System.out.println("Admin Y write portfolio board");
        return new ResponseDto<Integer>(HttpStatus.OK,1);
    }

    @GetMapping(value = "portboard/write")
    public String gowritePortfolio(){
        return "PortfolioBoard/portwrite";
    }




}
