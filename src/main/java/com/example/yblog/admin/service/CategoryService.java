package com.example.yblog.admin.service;


import com.example.yblog.model.BoardCategory;
import com.example.yblog.repository.BoardCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

    @Autowired
    BoardCategoryRepository boardCategoryRepository;



    @Transactional(readOnly = true)
    public Page<BoardCategory> categoryList(Pageable pageable){

        return boardCategoryRepository.findAll(pageable);
    }


    @Transactional
    public int SaveCategory(String catename , String thumnailfileRoot){
        BoardCategory boardCategory = new BoardCategory();


        boardCategory.setCategoryName(catename);
        boardCategory.setCategoryThunmbnail(thumnailfileRoot);

        boardCategoryRepository.save(boardCategory);


        return 1;
    }

    @Transactional
    public int delteCategory(int id){
        BoardCategory boardCategory = boardCategoryRepository.findById(id)
                        .orElseThrow(()->{
                            return new IllegalArgumentException("글 삭제 실패 해당 카테고리가 없습니다");
                        });

        
        boardCategoryRepository.delete(boardCategory);

        return 1;
    }


}
