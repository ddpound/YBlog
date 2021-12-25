package com.example.yblog.repository;

import com.example.yblog.model.PortfolioBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PortfolioRepository extends JpaRepository<PortfolioBoard, Integer> {

    @Query("SELECT id FROM PortfolioBoard")
    List<Integer> selectId();


}
