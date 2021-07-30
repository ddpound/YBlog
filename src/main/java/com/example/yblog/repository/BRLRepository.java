package com.example.yblog.repository;

import com.example.yblog.model.BoardReplyLimit;
import com.example.yblog.model.YUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BRLRepository extends JpaRepository<BoardReplyLimit, Integer> {

    Optional<BoardReplyLimit> findByUser(YUser yUser);

    void deleteAllByUser(YUser yUser);





}
