package com.example.yblog.join.controller;


import com.example.yblog.allstatic.AllStaticElement;
import com.example.yblog.dto.ResponseDto;
import com.example.yblog.join.service.JoinService;
import com.example.yblog.kakaoLogin.dto.KakaoProfile;
import com.example.yblog.kakaoLogin.service.KaKaoLoginService;
import com.example.yblog.login.service.LoginService;
import com.example.yblog.model.YUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// 인증이 안된 사용자들이 출입할수있는 /auth/** 허용
// 그냥 주소가 / 이면 index로 가게 혀용
// static 아래 있는 파일들도 허용해야함

@Controller
public class JoinController {


    @Autowired
    JoinService joinService;

    @Autowired
    LoginService loginService;

    @Autowired
    KaKaoLoginService kaKaoLoginService;

    @Autowired
    private BCryptPasswordEncoder encode;


    @GetMapping(value = "/auth/emailauth")
    public String goEmailAuthView(Model model){
        model.addAttribute("loginRequestURI", AllStaticElement.JoinRequestURI);

        return "loginJoin/emailauth";
    }

    @GetMapping(value = "auth/kakao/callback")
    public String kakaoJoinInfo(@RequestParam("code")String code, Model model){
        KakaoProfile kakaoProfile = kaKaoLoginService.intergration(code, AllStaticElement.redirect_uri);

        int resultNum = joinService.EmailDuplicateCheckl(kakaoProfile.getKakao_account().getEmail());

        if(resultNum ==1){
            model.addAttribute("errorM","이미 있는 사용자입니다");
            return "error/error";
        }

        model.addAttribute("authEmail",kakaoProfile.getKakao_account().getEmail());

        return "loginJoin/kakaojoin";
    }

    // 회원가입 시에는 인증권한을 시큐리티한테 받아내지 못한 상태니깐 uri 에 auth값이 들어가는게 맞다
    @PostMapping(value = "/auth/save")
    @ResponseBody
    public ResponseDto<Integer> saveUser(@RequestBody YUser yUser){
        int resultNum =joinService.saveUser(yUser);
        if(resultNum == 1){
            return new ResponseDto<Integer>(HttpStatus.OK,resultNum);
        }else if (resultNum == 0){ //중복에러시 반환하게만들었다
            return new ResponseDto<Integer>(HttpStatus.OK,0);
        }else{
            return new ResponseDto<Integer>(HttpStatus.INTERNAL_SERVER_ERROR,-1);
        }

        // 자바 오브젝트가 리턴되면 json으로 알아서 리턴해준다 jackSon 라이브러리가 알아서해줌(라이브러리가 없어도)
        // 그냥 알아서 반환해주는것 같다
    }





}
