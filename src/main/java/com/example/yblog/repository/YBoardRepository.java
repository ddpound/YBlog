package com.example.yblog.repository;

import com.example.yblog.model.YBoard;
import com.example.yblog.model.YUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface YBoardRepository extends JpaRepository<YBoard,Integer> {

    List<YBoard> findAllByUser(YUser yUser);

    void deleteAllByUser(YUser yUser);


    @Query("SELECT id FROM YBoard ")
    List<Integer> selectId();

    //search 부분
    @Query(
            value = "select *  from YBoard where title like %:title%",
            nativeQuery = true)
    List<YBoard> searchBoardTitle(@Param("title") String title);



}
