package com.example.yblog.repository;

import com.example.yblog.model.YUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

// 해당 레파지토리는 모델 YUSER를 관리하며 두번째  제네릭은 해당 프라이머리키의 형식 이라는뜻이다
// 이녀석은 스프링 DAO라고 생각하면 된다
// 스프링 IOC에서 가지고 있나요 하지만 얘는 자동 빈 등록이 된다
//@Repository //생략가능
public interface YUserRepository extends JpaRepository<YUser,Integer> {
    //  Select *From yuser where username= username
    Optional<YUser> findByUsername(String username);

    //  스프링 시큐리티 로그인 인증과정을 거칠 예정이니 주석처리 함
    // JPA Naming 전략
    // SELECT * FROM yuser WHERE username = username AND Password=password ; 이런쿼리가 알아서 로직이짜진다
    // 대문자 순으로 그리고 파라미터가 ? 값이 된다
    /*YUser findByUsernameAndPassword(String username, String password);*/

    // 이렇게 들어가도 된다
    /*@Query(value = "ELECT * FROM yuser WHERE username = ?1 AND Password=?2 " , nativeQuery=true)
    YUser login(String username,String password);*/

}
