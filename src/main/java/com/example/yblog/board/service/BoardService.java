package com.example.yblog.board.service;

import com.example.yblog.model.YBoard;
import com.example.yblog.model.YReply;
import com.example.yblog.model.YUser;
import com.example.yblog.repository.YBoardRepository;
import com.example.yblog.repository.YReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BoardService {
    @Autowired
    private YBoardRepository yBoardRepository;

    @Autowired
    private YReplyRepository yReplyRepository;

    @Transactional
    public int SaveBoard(YBoard yBoard, YUser yUser){
        yBoard.setUser(yUser);
        yBoard.setCount(0);
        yBoardRepository.save(yBoard);
        return 1;
    }
    @Transactional(readOnly = true)
    public List<YBoard> boardList(Pageable pageable){
        Page<YBoard> paging =yBoardRepository.findAll(pageable);
        List<YBoard> listBoard = paging.getContent();
        return listBoard;
    }
    @Transactional(readOnly = true)
    public Page<YBoard> boardListPage(Pageable pageable){
        return yBoardRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public YBoard boardDetails(int id){
        return yBoardRepository.findById(id)
                .orElseThrow(()->{
                    return new IllegalArgumentException("글 상세보기 실패 아이디를 찾을 수 없습니다");
                });
    }

    @Transactional
    public void delete(int id){
        YBoard yBoard = yBoardRepository.findById(id)
                .orElseThrow(()->{
                    return new IllegalArgumentException("글 상세보기 실패 아이디를 찾을 수 없습니다");
                });

        yReplyRepository.deleteAllByBoard(yBoard);

        yBoardRepository.deleteById(id);

    }

    @Transactional
    public void boardModify(YBoard yBoard){
        YBoard board = yBoardRepository.findById(yBoard.getId())
                .orElseThrow(()->{
                    return new IllegalArgumentException("글 찾기 실패 아이디를 찾을 수 없습니다");
                }); // 영속화 시키기
        board.setTitle(yBoard.getTitle());
        board.setContent(yBoard.getContent());
        // 해당함수로  Service가 종료와 함게 트랜잭션 종료, 이때 더티체킹으로 자동 업데이트된다

    }

    @Transactional
    public void saveReply(YUser yUser,int boardId ,YReply requestYReply){

        YBoard yBoard =  yBoardRepository.findById(boardId)
                .orElseThrow(()->{
                    return new IllegalArgumentException("댓글쓰기 실패 아이디를 찾을 수 없습니다");
                }); // 영속화 시키기;

        requestYReply.setUser(yUser);
        requestYReply.setBoard(yBoard);

        yReplyRepository.save(requestYReply);
    }

    @Transactional
    public void replyDelete(int replyId){

        yReplyRepository.deleteById(replyId);
    }

    @Transactional
    public YReply findYReply(int replyId){

        return yReplyRepository.findById(replyId)
                .orElseThrow(()->{
                    return new IllegalArgumentException("댓글찾기 실패");
                });
    }

}
