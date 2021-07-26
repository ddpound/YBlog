package com.example.yblog.repository;

import com.example.yblog.model.PortfolioBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioRepository extends JpaRepository<PortfolioBoard, Integer> {

}
