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
public class BoardCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private int id;

    @Column(nullable = false)
    private String categoryName;

    // 썸네일은 따로 만들어서 올릴예정
    @Column
    private String categoryThunmbnail;

    @CreationTimestamp // 시간이 자동으로 입력
    private Timestamp createDate;

}
