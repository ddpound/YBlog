package com.example.yblog.login.controller;


import com.example.yblog.allstatic.IpHostName;
import com.example.yblog.dto.ResponseDto;
import com.example.yblog.kakaoLogin.dto.KakaoProfile;
import com.example.yblog.kakaoLogin.service.KaKaoLoginService;
import com.example.yblog.login.service.LoginService;
import com.example.yblog.model.YUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
// 인증이 안된 사용자들도 접속할수 있게 만드는 경로 를 /auth/**로 할꺼임

@Controller
public class LoginController {

    @Autowired
    LoginService loginService;

    @Autowired
    KaKaoLoginService kaKaoLoginService;

    // 로그인창
    @GetMapping(value = "/auth/loginForm")
    public String loginView(){
        return "loginJoin/login";
    }

    @GetMapping(value = "/auth/kakao/login/callback")
    public String kakaoLogin(@RequestParam("code") String code, Model model){

        KakaoProfile kakaoProfile =  kaKaoLoginService.intergration(code , IpHostName.Loginredirect_uri);
        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", IpHostName.ContentType);

        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("", kakaoProfile.getKakao_account().getEmail());


        HttpEntity<MultiValueMap<String, String>> loginRequest = new HttpEntity<>(params,headers);
        rt.exchange("", HttpMethod.POST,loginRequest, String.class);


        return "/";
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
