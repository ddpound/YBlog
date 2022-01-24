package com.example.yblog.repository;

import com.example.yblog.model.BoardCategory;
import com.example.yblog.model.SkillBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    // 카테고리 삭제할때 사용함
    List<SkillBoard> findByBoardCategory(BoardCategory boardCategory);


    // 본격적으로 보여주려할때 사용함
    Page<SkillBoard> findAllByBoardCategory(BoardCategory boardCategory , Pageable pageable);



}
