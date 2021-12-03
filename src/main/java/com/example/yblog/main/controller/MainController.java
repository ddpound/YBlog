package com.example.yblog.main.controller;


import com.example.yblog.allstatic.AllStaticElement;
import com.example.yblog.board.service.BoardService;
import com.example.yblog.config.auth.PrincipalDetail;
import com.example.yblog.join.service.JoinService;
import com.example.yblog.login.service.LoginService;
import com.example.yblog.model.Status;
import com.example.yblog.model.YUser;
import com.example.yblog.status.StatusService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Log4j2
@Controller
public class MainController {

    @Value("${blogAdmin.adminName}")
    private String adminName;

    @Value(("${blogAdmin.adminEmail}"))
    private String adminEmail;

    @Autowired
    BoardService boardService;

    @Autowired
    StatusService statusService;

    @Autowired
    LoginService loginService;

    @Autowired
    JoinService joinService;

    Device device;

    // 메인
    @GetMapping(value = {"/" , "/index"})
    public  String mainView(HttpServletRequest request) {

        // 메인을 불러오면서 딱 한번 하는 부분
        if(AllStaticElement.statusNum < 1){
            // 현재 상태글 딱 한번  sql 에 저장해야하는 출력해주는 부분
            try {
                if (loginService.findUsername(adminName) != null){
                    log.info("Already Admin");
                }
            }catch (Exception e){
                // 에러 나는거 자체가 유저가 없다는 의미
                YUser yUser = new YUser();
                yUser.setEmail(adminEmail);
                yUser.setUsername(adminName);

                joinService.saveUser(yUser);
                log.info("Save Admin");
            }

            // 메인 페이지의 상태창을 말함
            Status status = new Status();
            status.setNowStatus("저장된 정보가 없습니다");
            statusService.SaveStatus(status);

            //주소값을 바로 받아와 주는 부분 (역시 statusNum 을 통해 딱 한번  업로드 시켜줌)
            log.info("Now RequestURL : "+ request.getRequestURL());
            AllStaticElement.StaticURL = request.getRequestURL().toString();
            String URL =  request.getRequestURL().toString();
            String Localredirect_uri = URL+"auth/kakao/callback";
            String LocalLoginredirect_uri = URL+"auth/kakao/login/callback";


            String LocalJoinRequestURI = "https://kauth.kakao.com/oauth/authorize?"
                    + "client_id="+ AllStaticElement.client_id
                    + "&redirect_uri="+URL+"auth/kakao/callback&response_type=code";
            String LocalLoginRequestURI = "https://kauth.kakao.com/oauth/authorize?"
                    + "client_id="+ AllStaticElement.client_id
                    + "&redirect_uri="+URL+"auth/kakao/login/callback&response_type=code";


            AllStaticElement.JoinRequestURI = LocalJoinRequestURI;
            AllStaticElement.redirect_uri = Localredirect_uri;
            AllStaticElement.Loginredirect_uri = LocalLoginredirect_uri;
            AllStaticElement.LoginRequestURI = LocalLoginRequestURI;

            AllStaticElement.statusNum ++;
        }

        device = DeviceUtils.getCurrentDevice(request);

        if (device.isMobile() || device.isTablet()){
            return "mIndex";
        }

        return "index";
    }

    // 보드 메인
    @GetMapping(value = "/auth/boardmain" )
    public  String mainBoardView(Model model , @PageableDefault(size = 5, sort = "id",direction = Sort.Direction.DESC)Pageable pagealbe,
                            HttpServletRequest request) {

        model.addAttribute("boards", boardService.boardList(pagealbe));
        model.addAttribute("boardsPage", boardService.boardListPage(pagealbe));
        model.addAttribute("nowStatus" , statusService.statusReturn(1));

        device = DeviceUtils.getCurrentDevice(request);

        if (device.isMobile() || device.isTablet()){
            return "board/mBoardMain";
        }


        return "board/boardMain";
    }



    @GetMapping(value = "/auth/board/details")
    public String boardDetails(@RequestParam("id") int id , Model model, HttpServletRequest request,
                               HttpServletResponse response){

        Cookie[] cookies = request.getCookies();
        int visit =0;

        if(cookies == null){
            Cookie cookie1 = new Cookie("visit",request.getParameter("id"));
            cookie1.setMaxAge(1800);
            response.addCookie(cookie1);
            boardService.boardCountUp(id);
        }else{
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("visit")){
                    visit =1;
                    if(cookie.getValue().contains(request.getParameter("id"))){
                        // 이미 있는 쿠키라서 아무것도 안함
                    }else{
                        cookie.setValue((cookie.getValue()+"_"+request.getParameter("id")));
                        cookie.setMaxAge(1800);
                        response.addCookie(cookie);
                        boardService.boardCountUp(id);
                    }
                }
            }

        }

        if(visit==0){
            Cookie cookie1 = new Cookie("visit",request.getParameter("id"));
            cookie1.setMaxAge(1800);
            response.addCookie(cookie1);
            boardService.boardCountUp(id);
        }


        model.addAttribute("board", boardService.boardDetails(id));

        return "board/boardDetails";
    }

    @GetMapping(value = "user/info")
    public String UserInfoPage(@RequestParam("username")String username ,@AuthenticationPrincipal PrincipalDetail principal,
                               Model model){
        YUser yUser = loginService.findUsername(username);

        if(principal.getYUser().getUsername().equals(yUser.getUsername())){
            model.addAttribute("YUser", yUser);
            return "user/userinfoPage";
        }
        return "redirect:/";
    }

    @DeleteMapping("/auth/user/delete")
    @ResponseBody
    public String deleteUser(@RequestBody YUser yUser, @AuthenticationPrincipal PrincipalDetail principal){
        if(principal.getUsername().equals(yUser.getUsername())){
            loginService.deleteUser(yUser);
        }


        return "{\"result\" : true}";
    }


}
