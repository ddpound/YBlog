package com.example.yblog.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class SkillBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private int id;

    @Column
    private String title;

    @Lob
    private String content;

    @Column
    private  int count;

    //보드가 Many 유저가 One (한명의 유저가 여러 게시글을 쓸수 있다.)
    // 다대일 관계
    @ManyToOne
    @JoinColumn(name = "userId") // board 테이블값에 YUser를 참조하는 userId를 생성한다
    private  YUser user; // DB는 오브젝트를 저장할 수 없다. FK 자바는 오브젝트를 저장할 수 있다.

    @OneToMany(mappedBy = "board", fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"board"})
    @OrderBy("id desc")
    private List<SkillBoardReply> skillBoardReplyList;

    // 직접 로컬에 저장시켜서 하는방법
    // 랜덤으로 해당 이미지 파일이 자동 저장됨
    @Column(length = 200)
    private  String imagefileid;

    // 구글 검색을 위한 설명칸이 필요함을 느낌
    @Column
    private String description;

    // 썸네일이있는게 이쁨 그래서 썸네일 애트리뷰트도 추가함
    @Column
    private String thumbnail;

    //카테고리 테이블 만들고 카테고리랑 조인 시킬꺼임


    // 기술보드 카테고리 하나의 카테고리는 여러 게시판과 관계를 가진다
    // null값이면 기본 아무소속없는 카테고리라고 칭한다
    @ManyToOne
    @JoinColumn(name = "category")
    private BoardCategory boardCategory;

    @CreationTimestamp
    private Timestamp createDate;



}
