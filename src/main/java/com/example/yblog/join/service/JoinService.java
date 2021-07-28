package com.example.yblog.join.service;

import com.example.yblog.model.YUser;
import com.example.yblog.repository.YUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JoinService {

    @Value("${ysj.key}")
    private String ysjKey;

    @Autowired
    YUserRepository yUserRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;


    @Transactional
    public int saveUser(YUser yUser){
        if(yUser.getPassword() == null || yUser.getPassword().equals("")){
            yUser.setPassword(ysjKey);
        }

        try {
            String rawPassword = yUser.getPassword();
            String encPassword = encoder.encode(rawPassword);
            yUser.setPassword(encPassword);
            yUserRepository.save(yUser);
            return 1;
        }catch (DataIntegrityViolationException  Integrity){
            // Integrity -> 무결성 위반에러 (즉 똑같은 이미 있는 값을 입력해서 중복되어서 무결성위반을 했을때 에러발생시킴)
            System.out.println("Violation of Integrity"); // 테스트해보니 잘잡힌다
            return 0; // 중복 주키
        }catch (Exception e){
            System.out.println("Join Error");
            e.printStackTrace();
            return -1; // 아직 직접확인못하는 알수없는 에러
        }

    }



}
