package com.example.yblog.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder // 빌더패턴
@Entity
public class YUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 오라클이면 시퀸스 , mySql이면 오토인크리먼트로 따라간다는 뜻
    private int id;

    @Column(nullable = false,length = 30)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false,length = 50)
    private  String email;

    @CreationTimestamp // 시간이 자동으로 입력
    private Timestamp createDate;
}
