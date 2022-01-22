package com.example.yblog.admin.controller;


import com.example.yblog.YBlogApplication;
import com.example.yblog.admin.service.AdminService;
import com.example.yblog.allstatic.AllStaticElement;
import com.example.yblog.board.service.BoardService;
import com.example.yblog.config.auth.PrincipalDetail;
import com.example.yblog.login.service.LoginService;
import com.example.yblog.model.YUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

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

            return "admin/allUserManage";
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
    public  String userEmailBan(@PathVariable("userEmail")String userEmail ,
                                @AuthenticationPrincipal PrincipalDetail principal){

        if(principal.getUsername().equals(adminName)){
            adminService.addBanEmail(userEmail);
        }

        return "redirect:/admin/userManageView";
    }

    // 제한 테이블 전부다 삭제버튼
    @GetMapping(value = "/admin/all_clear_BRlimit")
    public String allClearBRlimit(@AuthenticationPrincipal PrincipalDetail principal){

        if(principal.getUsername().equals(adminName)){
            adminService.deleteBRLBoard();
        }


        return "redirect:/admin/userManageView";
    }

    ExitCodeGenerator exitCodeGenerator;

    @GetMapping(value = "/admin/shut_down")
    public String shut_down(@AuthenticationPrincipal PrincipalDetail principal){

        if(principal.getUsername().equals(adminName)){
            AllStaticElement.ctx.close();
        }


        return "redirect:/admin/userManageView";
    }

    @Autowired
    ResourceLoader resourceLoader;

    // 스프링 ads 최상위 계층 접근시 반환해주는거
    @RequestMapping(value = "/admin/log.txt")
    public ResponseEntity<Resource> adstxt() {


        try {
            Resource resource;
            if(AllStaticElement.OsName.equals("window")){
                resource = resourceLoader.getResource("file:C:\\Users\\dpoun\\Desktop\\log\\logfile.log");
            }else{
                resource = resourceLoader.getResource("file:/home/youseongjung/Desktop/Yblog/logfile.log");
            }

            File file = resource.getFile();	//파일이 없는 경우 fileNotFoundException error가 난다.

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,file.getName())	//다운 받아지는 파일 명 설정
                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.length()))	//파일 사이즈 설정
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM.toString())	//바이너리 데이터로 받아오기 설정
                    .body(resource);	//파일 넘기기
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(null);
        } catch (Exception e ) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }


}
