package com.example.yblog.repository;

import com.example.yblog.model.YBoard;
import com.example.yblog.model.YUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface YBoardRepository extends JpaRepository<YBoard,Integer> {

    List<YBoard> findAllByUser(YUser yUser);

    void deleteAllByUser(YUser yUser);

    


}
