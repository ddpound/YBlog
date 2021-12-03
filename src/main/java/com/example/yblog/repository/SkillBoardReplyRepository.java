package com.example.yblog.repository;

import com.example.yblog.model.SkillBoard;
import com.example.yblog.model.SkillBoardReply;
import com.example.yblog.model.YUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillBoardReplyRepository extends JpaRepository<SkillBoardReply, Integer> {

    void deleteAllByBoard(SkillBoard skillBoard);

    void deleteAllByUser(YUser yUser);

}
