package com.example.yblog.handler;

import com.example.yblog.allstatic.IpHostName;
import org.springframework.stereotype.Component;

@Component
public class GlobalThrowError {

    public void ErrorMaxSqlLength(int boardLength){
        if(boardLength > IpHostName.sqlMaxLength){
            throw new IllegalArgumentException("게시판 글의 용량을 초과하셨습니다 사진은 최대 3mb까지 업로드가능!");
        }
    }

    public void ErrorNullBoardTitle(String boardTitle){
        if(boardTitle == null || boardTitle.equals("")){
            throw new IllegalArgumentException("서버로 게시판 제목이 비어서 도착했네요 제목은 꼭 써주세요!");
        }

    }



}
