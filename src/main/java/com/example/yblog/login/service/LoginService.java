package com.example.yblog.login.service;

import com.example.yblog.model.YUser;
import com.example.yblog.repository.BRLRepository;
import com.example.yblog.repository.YBoardRepository;
import com.example.yblog.repository.YReplyRepository;
import com.example.yblog.repository.YUserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Log4j2
@Service
public class LoginService {

    @Autowired
    YUserRepository yUserRepository;

    @Autowired
    YBoardRepository yBoardRepository;

    @Autowired
    YReplyRepository yReplyRepository;

    @Autowired
    BRLRepository brlRepository;

    @Transactional(readOnly = true)
    public  YUser findEmail(String email){
            return yUserRepository.findByEmail(email)
                    .orElseThrow(()-> new IllegalArgumentException("없는 사용자 입니다"));

    }

    @Transactional(readOnly = true)
    public YUser findUsername(String username){
        return yUserRepository.findByUsername(username)
                .orElseThrow(()->new IllegalArgumentException("없는 닉네임의 사용자입니다"));
    }

    @Transactional
    public void deleteUser(YUser yUser){
        Optional<YUser> localyUser = yUserRepository.findByUsername(yUser.getUsername());
        if(localyUser.isEmpty()){
            log.info("Fail DeleteUser find not User");
        }else{
            brlRepository.deleteAllByUser(localyUser.get());
            yReplyRepository.deleteAllByUser(localyUser.get());
            yBoardRepository.deleteAllByUser(localyUser.get());
            yUserRepository.deleteById(localyUser.get().getId());
        }




    }




    // 스프링 부트 시큐리티 사용으로 인한 아래 로그인 함수 삭제

  /*  // 이 유저값 자체를 리턴해준다
    @Transactional(readOnly = true) // select할때 트랜잭션 시작, 서비스 종료시 트랜잭션종료 (정합성 유지)
    public YUser loginUser(YUser yUser){
        return yUserRepository.findByUsernameAndPassword(yUser.getUsername(), yUser.getPassword());
    }*/



}
