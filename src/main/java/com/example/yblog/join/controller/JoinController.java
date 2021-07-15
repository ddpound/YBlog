package com.example.yblog.join.controller;


import com.example.yblog.dto.ResponseDto;
import com.example.yblog.join.service.JoinService;
import com.example.yblog.model.YUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

// 인증이 안된 사용자들이 출입할수있는 /auth/** 허용
// 그냥 주소가 / 이면 index로 가게 혀용
// static 아래 있는 파일들도 허용해야함

@Controller
public class JoinController {

    @Autowired
    JoinService joinService;

    @Autowired
    private BCryptPasswordEncoder encode;


    @GetMapping(value = "/auth/emailauth")
    public String goEmailAuthView(){

        return "loginJoin/emailauth";
    }

    // 회원가입 시에는 인증권한을 시큐리티한테 받아내지 못한 상태니깐 uri 에 auth값이 들어가는게 맞다
    @PostMapping(value = "/auth/save")
    @ResponseBody
    public ResponseDto<Integer> saveUser(@RequestBody YUser yUser){
        int resultNum =joinService.saveUser(yUser);

        if(resultNum == 1){
            return new ResponseDto<Integer>(HttpStatus.OK,resultNum);
        }else if (resultNum == 0){ //중복에러시 반환하게만들었다
            return new ResponseDto<Integer>(HttpStatus.INTERNAL_SERVER_ERROR,resultNum);
        }else{
            return new ResponseDto<Integer>(HttpStatus.INTERNAL_SERVER_ERROR,resultNum);
        }

        // 자바 오브젝트가 리턴되면 json으로 알아서 리턴해준다 jackSon 라이브러리가 알아서해줌(라이브러리가 없어도)
        // 그냥 알아서 반환해주는것 같다

    }




}
