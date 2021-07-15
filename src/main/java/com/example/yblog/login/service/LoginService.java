package com.example.yblog.login.service;

import com.example.yblog.model.YUser;
import com.example.yblog.repository.YUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoginService {

    @Autowired
    YUserRepository yUserRepository;




    // 스프링 부트 시큐리티 사용으로 인한 아래 로그인 함수 삭제

  /*  // 이 유저값 자체를 리턴해준다
    @Transactional(readOnly = true) // select할때 트랜잭션 시작, 서비스 종료시 트랜잭션종료 (정합성 유지)
    public YUser loginUser(YUser yUser){
        return yUserRepository.findByUsernameAndPassword(yUser.getUsername(), yUser.getPassword());
    }*/



}
