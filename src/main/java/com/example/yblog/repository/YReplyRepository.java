package com.example.yblog.repository;

import com.example.yblog.model.YBoard;
import com.example.yblog.model.YReply;
import com.example.yblog.model.YUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface YReplyRepository extends JpaRepository<YReply,Integer> {


    void deleteAllByBoard(YBoard Board);

    void deleteAllByUser(YUser yUser);



}
