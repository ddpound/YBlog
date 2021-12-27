package com.example.yblog.repository;

import com.example.yblog.model.PatchNote;
import com.example.yblog.model.PortfolioBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PatchNoteRepository extends JpaRepository<PatchNote,Integer> {

    @Query("SELECT id FROM PatchNote ")
    List<Integer> selectId();

    //search 부분
    @Query(
            value = "select *  from PatchNote where title like %:title%",
            nativeQuery = true)
    List<PatchNote> searchBoardTitle(@Param("title") String title);

}
