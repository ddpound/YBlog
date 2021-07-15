package com.example.yblog.config.auth;


import com.example.yblog.model.YUser;
import com.example.yblog.repository.YUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PrincipalDetailService implements UserDetailsService {

    @Autowired
    private YUserRepository yUserRepository;



    // 스프링이 로그인 요청을 가로챌때 username 변수랑 password 변수를 가로채서
    // password 부분처리는 알아서 함.
    // username이 DB에 있는지만 확인해주면됨
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        YUser principal = yUserRepository.findByUsername(username)
                .orElseThrow(()->{
                    return new UsernameNotFoundException("해당 사용자를 찾을수 없습니다 : " + username);
                });
        return new PrincipalDetail(principal); // 시큐리티 세션에 유저정보가 저장
    }
}
