package com.example.yblog.admin.controller;


import com.example.yblog.admin.service.AdminService;
import com.example.yblog.board.service.BoardService;
import com.example.yblog.config.auth.PrincipalDetail;
import com.example.yblog.login.service.LoginService;
import com.example.yblog.model.YUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class AdminController {

    @Value("${blogAdmin.adminName}")
    private  String adminName;

    @Autowired
    LoginService loginService;

    @Autowired
    BoardService boardService;

    @Autowired
    AdminService adminService;

    @GetMapping(value = "/admin/userManageView/find-all-user")
    public String findAllUser(@AuthenticationPrincipal PrincipalDetail principal,
                              Model model){

        if(principal.getUsername().equals(adminName)){
            model.addAttribute("allUser", adminService.findAllUser());

            return "admin/userManage";
        }
        return "redirect:/";
    }


    @GetMapping(value = "/admin/userManageView")
    public String goUserDeletePage(@AuthenticationPrincipal PrincipalDetail principal){
        if(principal.getUsername().equals(adminName)){



            // 매니저 보드판 만들기
            // 전체 조회수, 네트워크 접속과정에 관해  공부하기



            return "admin/userManage";
        }
        return "redirect:/";
    }

    @GetMapping(value = "/admin/userdelete/{userName}")
    public String deleteAdminUser(@PathVariable("userName")String userName,
                                  @AuthenticationPrincipal PrincipalDetail principal){
        if(principal.getUsername().equals(adminName)){
            if(userName.equals(adminName)){
                // 어드민이 어드민삭제를 방지
                return "redirect:/admin/userManageView";
            }

            YUser yUser = loginService.findUsername(userName);
            loginService.deleteUser(yUser);
            System.out.println("UserDeleteComplete");
        }

        return "redirect:/admin/userManageView";
    }


    @GetMapping(value = "/admin/delete/userboard/{userName}")
    public String DeleteBoardofUser(@PathVariable("userName")String userName,
                                    @AuthenticationPrincipal PrincipalDetail principal){
        if(principal.getUsername().equals(adminName)){
            YUser yUser = loginService.findUsername(userName);
            boardService.deleteAllByUser(yUser);
            System.out.println("CompleteUserBoardDeleteAll");
            // compliteUserBoardDeleteAll
        }
        return "redirect:/admin/userManageView";
    }

    @GetMapping(value = "/admin/delete/allboard")
    public String DeleteAllBoard(@AuthenticationPrincipal PrincipalDetail principal){
        if(principal.getUsername().equals(adminName)){
            boardService.deleteAllBoard();
            System.out.println("CompleteDeleteAll");
        }

        return "redirect:/admin/userManageView";
    }

    @GetMapping(value = "/admin/delete/ipall")
    public  String IpDeleteAll(@AuthenticationPrincipal PrincipalDetail principal){
        if(principal.getUsername().equals(adminName)){
            adminService.IpdeleteAll();
            System.out.println("CompleteDeleteAll Ip");
        }

        return "redirect:/admin/userManageView";
    }

    @GetMapping(value = "/admin/deleteip/{ip}")
    public String deleteIp(@PathVariable("ip")String ip ,@AuthenticationPrincipal PrincipalDetail principal){
        if(principal.getUsername().equals(adminName)){
            adminService.deleteIp(ip);
            System.out.println("CompleteSave Ip");
        }

        return "redirect:/admin/userManageView";
    }

    @GetMapping(value = "/admin/emailBan/{userEmail}")
    public  String userEmailBan(@PathVariable("userEmail")String userEmail ,@AuthenticationPrincipal PrincipalDetail principal){

        if(principal.getUsername().equals(adminName)){
            adminService.addBanEmail(userEmail);
        }

        return "redirect:/admin/userManageView";
    }


}
