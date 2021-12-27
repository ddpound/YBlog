package com.example.yblog.repository;

import com.example.yblog.model.SkillBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SkillBoardRepository extends JpaRepository<SkillBoard, Integer> {

    @Query("SELECT id FROM SkillBoard ")
    List<Integer> selectId();



    //search 부분
    @Query(
            value = "select *  from SkillBoard where title like %:title%",
            nativeQuery = true)
    List<SkillBoard> searchBoardTitle(@Param("title") String title);

}
