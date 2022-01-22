package com.example.yblog.repository;

import com.example.yblog.model.BoardCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardCategoryRepository extends JpaRepository<BoardCategory,Integer> {



}
