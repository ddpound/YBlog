package com.example.yblog.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class SkillBoardReply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 200)
    private String content; // 섬머노트 라이브러리 <html>태그가 섞여서 디자인됨

    @ManyToOne
    @JoinColumn(name = "boardId")
    private SkillBoard board;

    @ManyToOne
    @JoinColumn(name = "userId")
    private  YUser user;

    @CreationTimestamp
    private Timestamp createDate;


}
