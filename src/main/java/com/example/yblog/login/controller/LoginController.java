package com.example.yblog.login.controller;


import com.example.yblog.allstatic.AllStaticElement;

import com.example.yblog.kakaoLogin.dto.KakaoProfile;
import com.example.yblog.kakaoLogin.service.KaKaoLoginService;
import com.example.yblog.login.service.LoginService;

import com.example.yblog.model.YUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;

// 인증이 안된 사용자들도 접속할수 있게 만드는 경로 를 /auth/**로 할꺼임

@Controller
public class LoginController {

    @Value("${ysj.key}")
    private String ysjKey;

    @Autowired
    LoginService loginService;

    @Autowired
    KaKaoLoginService kaKaoLoginService;

    @Autowired
    AuthenticationManager authenticationManager;



    // 로그인창
    @GetMapping(value = "/auth/loginForm")
    public String loginView(Model model){
        model.addAttribute("Loginredirect_uri", AllStaticElement.LoginRequestURI);

        return "loginJoin/login";
    }


    @GetMapping(value = "/auth/kakao/login/callback")
    public String kakaoLogin(@RequestParam("code") String code, Model model){

        // 여기서 프로파일을 가져온다는 자체가 인증이 끝난 상태를 말하니깐
        KakaoProfile kakaoProfile =  kaKaoLoginService.intergration(code , AllStaticElement.Loginredirect_uri);

        YUser yUser =  loginService.findEmail(kakaoProfile.getKakao_account().getEmail());
        if(yUser == null){
           System.out.println("유저가 없습니다");
            return "redirect:/";
        }

        // 먼저 이 카카오 유저가 있는 회원인지를 검사하기로함, 그리고 비밀번호 체크후에
        // Auth Manager가 있고 로그인 처리를 또 해주는 녀석이 있는듯 그걸이용해서
        // 여기에 그 과정을 넣어주고 인덱스로 날려주면 될듯
        // 즉 강제세션 부여가 될듯

        if(loginService.checkBanEmail(yUser.getEmail())){
            System.out.println(yUser.getEmail()+" user is ban");
            return "redirect:/";
        }

        //세션등록
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(yUser.getUsername() , ysjKey));
        SecurityContextHolder.getContext().setAuthentication(authentication);


        return "redirect:/";
    }

    // 시큐리티 과정으로 바꿀꺼니깐 주석처리
  /*  @PostMapping(value = "try")
    @ResponseBody
    public ResponseDto<Integer> loginTry(@RequestBody YUser yUser, HttpSession session){
        System.out.println("로그인 시도 서버단 유저이름 : "+ yUser.getUsername());
        YUser principal = loginService.loginUser(yUser); //principal 접근주체
        if (principal != null){
            System.out.println("로그인 시도 서버단 유저이름 : "+ principal.getUsername());
            session.setAttribute("principal", principal);
            return new ResponseDto<>(HttpStatus.OK,1);
        }else{
            System.out.println("널값임");
            return new ResponseDto<>(HttpStatus.INTERNAL_SERVER_ERROR,-1);
        }


    }*/


}
