package com.example.yblog.board.service;

import com.example.yblog.admin.service.AdminService;
import com.example.yblog.handler.GlobalThrowError;
import com.example.yblog.model.BoardReplyLimit;
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

import java.io.File;
import java.util.List;

@Service
public class BoardService {
    @Autowired
    private YBoardRepository yBoardRepository;

    @Autowired
    private YReplyRepository yReplyRepository;

    @Autowired
    private GlobalThrowError globalThrowError;

    @Autowired
    AdminService adminService;


    @Transactional
    public int SaveBoard(YBoard yBoard, YUser yUser) {

        globalThrowError.ErrorMaxSqlLength(yBoard.getContent().length());

        globalThrowError.ErrorNullBoardTitle(yBoard.getTitle());
        BoardReplyLimit boardReplyLimit = adminService.findbrlRepositoryUser(yUser);

        if (boardReplyLimit == null) {
            adminService.FirstSaveLimitTable(yUser);
            yBoard.setUser(yUser);

            yBoard.setCount(0);
            yBoardRepository.save(yBoard);
            return 1;

        } else {
            if (boardReplyLimit.getBoardCount() < 30) {
                adminService.BRLimitboardADD(yUser);

                if (yBoard.getTitle() == null) {
                    System.out.println("do not Save because no title");
                    return -1;
                } else {
                    yBoard.setUser(yUser);

                    yBoard.setCount(0);

                    ImageSearch(yUser.getUsername());
                    yBoardRepository.save(yBoard);
                    return 1;

                }

            }
            System.out.println("30 up!");
            return -1;


        }

    }

    @Transactional(readOnly = true)
    public List<YBoard> boardList(Pageable pageable) {
        Page<YBoard> paging = yBoardRepository.findAll(pageable);
        List<YBoard> listBoard = paging.getContent();
        return listBoard;
    }

    @Transactional(readOnly = true)
    public Page<YBoard> boardListPage(Pageable pageable) {
        return yBoardRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public YBoard boardDetails(int id) {
        return yBoardRepository.findById(id)
                .orElseThrow(() -> {
                    return new IllegalArgumentException("글 상세보기 실패 아이디를 찾을 수 없습니다");
                });
    }

    @Transactional
    public void delete(int id) {
        YBoard yBoard = yBoardRepository.findById(id)
                .orElseThrow(() -> {
                    return new IllegalArgumentException("글 상세보기 실패 아이디를 찾을 수 없습니다");
                });

        yReplyRepository.deleteAllByBoard(yBoard);

        yBoardRepository.deleteById(id);

    }

    @Transactional
    public void boardModify(YBoard yBoard) {
        YBoard board = yBoardRepository.findById(yBoard.getId())
                .orElseThrow(() -> {
                    return new IllegalArgumentException("글 찾기 실패 아이디를 찾을 수 없습니다");
                }); // 영속화 시키기
        board.setTitle(yBoard.getTitle());
        board.setContent(yBoard.getContent());
        // 해당함수로  Service가 종료와 함게 트랜잭션 종료, 이때 더티체킹으로 자동 업데이트된다

    }

    @Transactional
    public void deleteAllByUser(YUser yUser) {
        List<YBoard> yBoardList = yBoardRepository.findAllByUser(yUser);
        for (int i = 0; i < yBoardList.size(); i++) {
            yReplyRepository.deleteAllByBoard(yBoardList.get(i));
        }

        yBoardRepository.deleteAllByUser(yUser);
    }

    @Transactional
    public void deleteAllBoard() {
        yReplyRepository.deleteAll();
        yBoardRepository.deleteAll();


    }


    @Transactional
    public void saveReply(YUser yUser, int boardId, YReply requestYReply) {

        BoardReplyLimit boardReplyLimit = adminService.findbrlRepositoryUser(yUser);

        YBoard yBoard = yBoardRepository.findById(boardId)
                .orElseThrow(() -> {
                    return new IllegalArgumentException("댓글쓰기 실패 아이디를 찾을 수 없습니다");
                }); // 영속화 시키기;


        if (boardReplyLimit == null) {
            adminService.FirstSaveLimitTable(yUser);
            requestYReply.setUser(yUser);
            requestYReply.setBoard(yBoard);

            yReplyRepository.save(requestYReply);
        } else {
            if (boardReplyLimit.getReplyCount() < 30) {
                adminService.BRLimitReplyADD(yUser);
                requestYReply.setUser(yUser);
                requestYReply.setBoard(yBoard);

                yReplyRepository.save(requestYReply);
            } else {
                System.out.println("30up!!");
                throw new IllegalArgumentException("30 count up");
            }

        }

    }

    @Transactional
    public void replyDelete(int replyId) {

        yReplyRepository.deleteById(replyId);
    }

    @Transactional
    public YReply findYReply(int replyId) {

        return yReplyRepository.findById(replyId)
                .orElseThrow(() -> {
                    return new IllegalArgumentException("댓글찾기 실패");
                });
    }

    // 임시 파일에있던 사진들을 검색 -> 이름변환 옮겨저장함함

    public void ImageSearch(String username){
        String temporary = "C:\\temporary_storage"; // 임시 파일 저장경로
        String fileRoot = "C:\\Confirm_SaveImage";	//저장확정 경로

        File dir = new File(temporary);
        File[] files = dir.listFiles();
        for(File f : files) {
            System.out.println("파일 이름체크");
            System.out.println("UserName-"+username);
            if(f.isFile() && f.getName().toUpperCase().startsWith("UserName-"+username)) {
                System.out.println(f.getName());
                System.out.println("파일 이름체크");
            }
        }

    }


}






