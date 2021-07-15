package com.example.yblog.repository;

import com.example.yblog.model.YBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface YBoardRepository extends JpaRepository<YBoard,Integer> {

}
