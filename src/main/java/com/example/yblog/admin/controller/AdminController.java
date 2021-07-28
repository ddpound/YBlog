package com.example.yblog.admin.controller;


import com.example.yblog.config.auth.PrincipalDetail;
import com.example.yblog.login.service.LoginService;
import com.example.yblog.model.YUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class AdminController {

    @Value("${blogAdmin.adminName}")
    private  String adminName;

    @Autowired
    LoginService loginService;


    @GetMapping(value = "/admin/userManageView")
    public String goUserDeletePage(@AuthenticationPrincipal PrincipalDetail principal){
        if(principal.getUsername().equals(adminName)){
            return "admin/userManage";
        }



        return "redirect:/";
    }

    @GetMapping(value = "/admin/userdelete/{userName}")
    public String deleteAdminUser(@PathVariable("userName")String userName,
                                  @AuthenticationPrincipal PrincipalDetail principal){
        if(principal.getUsername().equals(adminName)){
            YUser yUser = loginService.findUsername(userName);

            loginService.deleteUser(yUser);


            return "redirect:/admin/userManageView";
        }



        return "redirect:/admin/userManageView";
    }



}
