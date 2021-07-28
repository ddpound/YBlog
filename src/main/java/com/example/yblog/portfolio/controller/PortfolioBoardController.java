package com.example.yblog.portfolio.controller;


import com.example.yblog.allstatic.IpHostName;
import com.example.yblog.config.auth.PrincipalDetail;
import com.example.yblog.dto.ResponseDto;
import com.example.yblog.model.PortfolioBoard;
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

        if (principal.getUsername().equals(IpHostName.adminUser)){
            portfolioBoardService.portBoardSave(portfolioBoard,principal);
            System.out.println("Admin Y write portfolio board");
            return new ResponseDto<Integer>(HttpStatus.OK,1);
        }


        return new ResponseDto<Integer>(HttpStatus.INTERNAL_SERVER_ERROR,-1);
    }

    @GetMapping(value = "portboard/write")
    public String gowritePortfolio(@AuthenticationPrincipal PrincipalDetail principal ){

        if (principal.getUsername().equals(IpHostName.adminUser)){
            return "PortfolioBoard/portwrite";
        }else{
            return "redirect:/";
        }

    }
    @GetMapping(value = "/auth/portboard/details")
    public String portboardDetails(@RequestParam("id")int id, Model model){
        model.addAttribute("portboard",portfolioBoardService.detailsPortPolio(id));

        return "PortfolioBoard/portboardDetails";
    }

    @GetMapping("/portboard/goPortBoardmodify/{boardId}")
    public String goPortModifyPage(@PathVariable("boardId")int boardId,Model model){
        model.addAttribute("portboard",portfolioBoardService.detailsPortPolio(boardId));

        return "PortfolioBoard/boardPortModifyDetails";
    }
    @PutMapping(value = "/portboard/modify")
    @ResponseBody
    public ResponseDto<Integer> portboardModify(@RequestBody PortfolioBoard portfolioBoard, @AuthenticationPrincipal PrincipalDetail principal){
        if (principal.getUsername().equals(IpHostName.adminUser)){
            portfolioBoardService.portBoardModify(portfolioBoard);

            return new ResponseDto<Integer>(HttpStatus.OK,1);
        }else {
            return new ResponseDto<Integer>(HttpStatus.INTERNAL_SERVER_ERROR,-1);
        }


    }

    @DeleteMapping(value = "/portboard/delete/{boardId}")
    @ResponseBody
    public  ResponseDto<Integer> portboardDelete(@PathVariable("boardId")int boardId, @AuthenticationPrincipal PrincipalDetail principal){
        if (principal.getUsername().equals(IpHostName.adminUser)){
            portfolioBoardService.portBoardDelete(boardId);
            return new ResponseDto<Integer>(HttpStatus.OK,1);
        }else{
            return new ResponseDto<Integer>(HttpStatus.INTERNAL_SERVER_ERROR,-1);
        }
    }


}
