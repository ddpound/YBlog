package com.example.yblog.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class BoardReplyLimit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private  int id;

    @ColumnDefault(value = "0")
    private  int boardCount;

    @ColumnDefault(value = "0")
    private int replyCount;

    //보드가 Many 유저가 One (한명의 유저가 여러 게시글을 쓸수 있다.)
    // 다대일 관계
    @OneToOne
    @JoinColumn(name = "userId") // board 테이블값에 YUser를 참조하는 userId를 생성한다
    private  YUser user; // DB는 오브젝트를 저장할 수 없다. FK 자바는 오브젝트를 저장할 수 있다.



}
