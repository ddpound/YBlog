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
@Builder // 빌더패턴
@Entity
public class YBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private int id;

    @Column(nullable = false,length = 100)
    private String title;

    @Lob
    private String content; // 섬머노트 라이브러리 <html>태그가 섞여서 디자인됨

    @Column
    private  int count;

    //보드가 Many 유저가 One (한명의 유저가 여러 게시글을 쓸수 있다.)
    // 다대일 관계
    @ManyToOne
    @JoinColumn(name = "userId") // board 테이블값에 YUser를 참조하는 userId를 생성한다
    private  YUser user; // DB는 오브젝트를 저장할 수 없다. FK 자바는 오브젝트를 저장할 수 있다.

    @OneToMany(mappedBy = "board" , fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"board"})
    @OrderBy("id desc")
    private List<YReply> yreplys;

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



    @CreationTimestamp
    private Timestamp createDate;

}