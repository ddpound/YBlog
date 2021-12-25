package com.example.yblog.repository;

import com.example.yblog.model.SkillBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SkillBoardRepository extends JpaRepository<SkillBoard, Integer> {

    @Query("SELECT id FROM SkillBoard ")
    List<Integer> selectId();

}
