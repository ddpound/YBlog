package com.example.yblog.skillboard.controller;


import com.example.yblog.skillboard.service.SkillBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 보안없이 접근 가능한 컨트롤러
@Controller
@RequestMapping(value = "auth/skillboard")
public class AuthSkillBoardController {

    @Autowired
    SkillBoardService skillBoardService;

    Device device;

    @GetMapping("")
    public String mainView(Model model,
                           @PageableDefault(size = 5,
                                   sort = "id",
                                   direction = Sort.Direction.DESC) Pageable pageable,
                           HttpServletRequest httpServletRequest){

        model.addAttribute("boards", skillBoardService.skillBoardList(pageable));
        model.addAttribute("boardsPage",skillBoardService.skillBoardPage(pageable));




        // 후에 모바일 인식을 위함
        device = DeviceUtils.getCurrentDevice(httpServletRequest);

        if(device.isMobile() || device.isTablet()){
            return "skillBoard/mSkillBoardMain";
        }


        return "skillBoard/skillBoardMain";
    }


    @GetMapping(value = "details/{skillboardId}")
    public String skillBoardDetails(Model model, @PathVariable int skillboardId,
                                    HttpServletRequest request,
                                    HttpServletResponse response){

        String skillboardIdVisit =  Integer.toString(skillboardId);
        Cookie[] cookies = request.getCookies();
        int visit =0;

        if(cookies == null){
            Cookie cookie1 = new Cookie("skillvisit",skillboardIdVisit);
            cookie1.setMaxAge(1800);
            response.addCookie(cookie1);
            skillBoardService.boardCountUp(skillboardId);
        }else{
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("skillvisit")){
                    visit =1;
                    if(cookie.getValue().contains(skillboardIdVisit)){
                        // 이미 있는 쿠키라서 아무것도 안함
                    }else{
                        cookie.setValue((cookie.getValue()+"_"+skillboardIdVisit));
                        cookie.setMaxAge(1800);
                        response.addCookie(cookie);
                        skillBoardService.boardCountUp(skillboardId);
                    }
                }
            }

        }

        if(visit==0){
            Cookie cookie1 = new Cookie("skillvisit", skillboardIdVisit);
            cookie1.setMaxAge(1800);
            response.addCookie(cookie1);
            skillBoardService.boardCountUp(skillboardId);
        }




        model.addAttribute("board", skillBoardService.skillBoardDetails(skillboardId));


        return "skillBoard/skillBoardDetails";
    }




}
