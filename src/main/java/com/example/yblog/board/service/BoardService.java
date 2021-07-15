package com.example.yblog.board.service;

import com.example.yblog.model.YBoard;
import com.example.yblog.model.YUser;
import com.example.yblog.repository.YBoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BoardService {
    @Autowired
    YBoardRepository yBoardRepository;

    @Transactional
    public int SaveBoard(YBoard yBoard, YUser yUser){
        yBoard.setUser(yUser);
        yBoard.setCount(0);
        yBoardRepository.save(yBoard);


        return 1;
    }

    public List<YBoard> boardList(Pageable pageable){
        Page<YBoard> paging =yBoardRepository.findAll(pageable);
        List<YBoard> listBoard = paging.getContent();
        return listBoard;
    }

    public Page<YBoard> boardListPage(Pageable pageable){
        return yBoardRepository.findAll(pageable);
    }


}
