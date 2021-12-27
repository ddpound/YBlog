package com.example.yblog.repository;

import com.example.yblog.model.PortfolioBoard;
import com.example.yblog.model.SkillBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PortfolioRepository extends JpaRepository<PortfolioBoard, Integer> {

    @Query("SELECT id FROM PortfolioBoard")
    List<Integer> selectId();



    //search 부분
    @Query(
            value = "select *  from PortfolioBoard where title like %:title%",
            nativeQuery = true)
    List<PortfolioBoard> searchBoardTitle(@Param("title") String title);


}
