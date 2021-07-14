package com.example.yblog.join.controller;


import com.example.yblog.dto.ResponseDto;
import com.example.yblog.join.service.JoinService;
import com.example.yblog.model.YUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;



@Controller
@RequestMapping(value = "join")
public class JoinController {

    @Autowired
    JoinService joinService;


    @GetMapping(value = "emailauth")
    public String goEmailAuthView(){

        return "loginJoin/emailauth";
    }

    @PostMapping(value = "save")
    @ResponseBody
    public ResponseDto<Integer> saveUser(@RequestBody YUser yUser){
        System.out.println("가져온 유저 이름 : "+yUser.getUsername());
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
