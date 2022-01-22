package com.example.yblog.admin.service;


import com.example.yblog.model.BoardCategory;
import com.example.yblog.repository.BoardCategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    BoardCategory boardCategory;

    BoardCategoryRepository boardCategoryRepository;

    public int SaveCategory(BoardCategory boardCategory){


        
        boardCategoryRepository.save(boardCategory);
        return 1;


    }


}
