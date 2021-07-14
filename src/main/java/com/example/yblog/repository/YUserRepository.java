package com.example.yblog.repository;

import com.example.yblog.model.YUser;
import org.springframework.data.jpa.repository.JpaRepository;
// 해당 레파지토리는 모델 YUSER를 관리하며 두번째  제네릭은 해당 프라이머리키의 형식 이라는뜻이다
// 이녀석은 스프링 DAO라고 생각하면 된다
// 스프링 IOC에서 가지고 있나요 하지만 얘는 자동 빈 등록이 된다
//@Repository //생략가능
public interface YUserRepository extends JpaRepository<YUser,Integer> {




}
