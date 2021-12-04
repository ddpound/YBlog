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

import javax.servlet.http.HttpServletRequest;

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
    public String skillBoardDetails(Model model, @PathVariable int skillboardId){

        model.addAttribute("board", skillBoardService.skillBoardDetails(skillboardId));


        return "skillBoard/skillBoardDetails";
    }




}
