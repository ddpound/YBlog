package com.example.yblog.board.service;

import com.example.yblog.admin.service.AdminService;
import com.example.yblog.handler.GlobalThrowError;
import com.example.yblog.model.BoardReplyLimit;
import com.example.yblog.model.YBoard;
import com.example.yblog.model.YReply;
import com.example.yblog.model.YUser;
import com.example.yblog.repository.YBoardRepository;
import com.example.yblog.repository.YReplyRepository;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.List;
import java.util.UUID;

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

        YBoard chyBoard = new YBoard();

        // 오늘 처음으로 글을썼을때
        if (boardReplyLimit == null) {
            adminService.FirstSaveLimitTable(yUser);

            yBoard.setUser(yUser);
            yBoard.setCount(0);



            // 사진과 관련된 코드 세줄
            chyBoard = ImageSearch(yUser.getUsername(), yBoard,null,false);

            if(chyBoard ==null){
                return -2; // 사진 갯수가 10개가 넘어가는 경우
            }

            // 파일 경로 변경해주고 임시파일 -> 저장파일로 옮기는 과정
            yBoard.setContent(chyBoard.getContent());
            yBoard.setImagefileid(chyBoard.getImagefileid());

            yBoardRepository.save(yBoard);

            deletetemporaryStorage(yUser.getUsername(), true);
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



                    chyBoard = ImageSearch(yUser.getUsername(), yBoard,null,false);

                    // 파일 경로 변경해주고 임시파일 -> 저장파일로 옮기는 과정

                    if(chyBoard == null){
                        return -2; // 사진 갯수가 10개가 넘어가는 경우
                    }


                    yBoard.setContent(chyBoard.getContent());
                    yBoard.setImagefileid(chyBoard.getImagefileid());

                    yBoardRepository.save(yBoard);

                    // 저장 다하고 해당유저의 임시파일을 삭제하는 과정
                    deletetemporaryStorage(yUser.getUsername(), true);
                    return 1;

                }

            }
            System.out.println("30 up!");
            return -3; // 최대 게시글 30개 작성했을때 거절문


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

        // 관련된 사진파일을 삭제하기
        deleteConfirmfile(yBoard.getImagefileid());

        yBoardRepository.deleteById(id);

    }

    @Transactional
    public void boardModify(YBoard yBoard) {
        YBoard board = yBoardRepository.findById(yBoard.getId())
                .orElseThrow(() -> {
                    return new IllegalArgumentException("글 찾기 실패 아이디를 찾을 수 없습니다");
                }); // 영속화 시키기
        YBoard board1 = ImageSearch(board.getUser().getUsername(), yBoard, board,true);


        board.setTitle(yBoard.getTitle());
        board.setContent(board1.getContent());


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

    // 1. 파일들을 이름 변경해서 본파일로 옮김
    // 2. contents 안에있는 사진 src값을 변경 (board를)
    // 3. 임시파일 사진들을 전부 삭제
    // modify => 수정이면 참, 거짓이면 새로생성
    public YBoard ImageSearch(String username , YBoard yBoard, YBoard yBoard1,boolean modify)  {
        String temporary = "C:\\temporary_storage\\"+username; // 임시 파일 저장경로
        String fileRoot =null;
        String BoardTitleUUID =null;
        String chfileRoot =null;


        // 수정인걸 알았으니깐 이미 있는 파일 이름만 가져오면 됨
        if(modify){
            BoardTitleUUID = yBoard1.getImagefileid();
            fileRoot = "C:\\Confirm_SaveImage\\"+BoardTitleUUID+"\\";
            chfileRoot = "/Confirm_SaveImage/"+BoardTitleUUID+"/";
        }else{
            // 지금 새로만드는 파일이니깐 새로운 랜덤 키값을 지정
            BoardTitleUUID = UUID.randomUUID()+"-"+yBoard.getUser().getId();
            fileRoot = "C:\\Confirm_SaveImage\\"+BoardTitleUUID+"\\";	//저장확정 경로
            chfileRoot = "/Confirm_SaveImage/"+BoardTitleUUID+"/";
        }

        File dir = new File(temporary);
        File[] files = dir.listFiles();

        if(files ==null){
            System.out.println("imageis Not add");
        }else{
            if(files.length >10){
                return null;
            }

            for(File f : files) {

                String SearchfileName ="UserName-"+username;

                if(f.isFile() && f.getName().startsWith(SearchfileName)) {
                    // 이름 변경 -> 파일 이동 -> 오리지널 파일로 이동 url 는 그럼?
                    // DB 이름도 변경해야함 Content 검사해서 변경해서 넣어주기
                    System.out.println(f.getName());
                    String changeFileName =fileRoot+f.getName();

                    // 저장할 파일의 경로 (걍로와 이름)
                    File targetFile = new File(changeFileName);
                    try {
                        FileInputStream fileInputStream = new FileInputStream(f); // 저장할 파일
                        FileUtils.copyInputStreamToFile(fileInputStream, targetFile); //저장

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }
        }


        System.out.println(yBoard.getContent());
        String ChContent = yBoard.getContent().replace("/temporary_storage/"+username+"/", chfileRoot);
        System.out.println(ChContent);

        yBoard.setContent(ChContent);
        yBoard.setImagefileid(BoardTitleUUID);

        return yBoard;
    }

    public void deletetemporaryStorage(String username, boolean writebool){
        String path = "C:\\temporary_storage\\"+username;
        File folder = new File(path);
        try {
            while(folder.exists()) {
                File[] folder_list = folder.listFiles(); //파일리스트 얻어오기
                if(folder_list != null){
                    for (int j = 0; j < folder_list.length; j++) {
                        folder_list[j].delete(); //파일 삭제
                        System.out.println("All delete Temporary File UserName : "+username);

                    }

                    if(folder_list.length == 0 && folder.isDirectory()){
                        folder.delete(); //대상폴더 삭제
                        System.out.println("All delete Temporary Folder UserName : "+username);
                    }

                }else{
                    System.out.println("imageFolder and file is null");
                }

            }
        } catch (Exception e) {
            //참이면 글쓴거임
            if(writebool){
                System.out.println("write after delete imageFile");
            }else{
                // else면 글을 쓰지않고 나갓을때
                e.getStackTrace();
            }
        }
    }

    public void deleteConfirmfile(String filename){
        String path = "C:\\Confirm_SaveImage\\"+filename;
        File folder = new File(path);
        try {
            while(folder.exists()) {
                File[] folder_list = folder.listFiles(); //파일리스트 얻어오기

                for (int j = 0; j < folder_list.length; j++) {
                    folder_list[j].delete(); //파일 삭제
                    System.out.println("All delete Temporary File UserName : "+filename);

                }

                if(folder_list.length == 0 && folder.isDirectory()){
                    folder.delete(); //대상폴더 삭제
                    System.out.println("All delete Temporary Folder UserName : "+filename);
                }
            }
        } catch (Exception e) {
            e.getStackTrace();

        }
    }

    @Transactional
    public void boardCountUp(int boardid) {
        YBoard board = yBoardRepository.findById(boardid)
                .orElseThrow(() -> {
                    return new IllegalArgumentException("글 찾기 실패 아이디를 찾을 수 없습니다");
                }); // 영속화 시키기

        int countNum = board.getCount();

        board.setCount(countNum+1);


        // 해당함수로  Service가 종료와 함게 트랜잭션 종료, 이때 더티체킹으로 자동 업데이트된다

    }

    // 수정 로직 안에 파일 점검해서 게시판에 없는 녀석을 찾아내면 될듯
    // 즉 위의 애초에 작성로직에서 리스트를 쭉 받고 없는 녀석이라면 삭제해내는게 먼저
    // 용량을 아끼기위함

}






