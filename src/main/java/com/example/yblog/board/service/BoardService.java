package com.example.yblog.board.service;

import com.example.yblog.admin.service.AdminService;
import com.example.yblog.allstatic.AllStaticElement;
import com.example.yblog.handler.GlobalThrowError;
import com.example.yblog.model.BoardReplyLimit;
import com.example.yblog.model.YBoard;
import com.example.yblog.model.YReply;
import com.example.yblog.model.YUser;
import com.example.yblog.repository.YBoardRepository;
import com.example.yblog.repository.YReplyRepository;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
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

        if(scriptSearch(yBoard.getContent()) ||
                scriptSearch(yBoard.getTitle()) ||
                scriptSearch(yBoard.getDescription())){

            return -4; // 스크립트문 검색 발견
        }


        if(yBoard.getTitle().replaceAll(" ", "").equals("") ||
        yBoard.getContent().replaceAll(" ","").equals("") ||
        yBoard.getContent().length()==0 ||
        yBoard.getTitle().length()==0) {
            return -5;// 제목이나 내용이 한개도 없을때
        }

        if(yBoard.getTitle().length()>50 || yBoard.getContent().length() > 2000){
            return -6;
        }

        globalThrowError.ErrorMaxSqlLength(yBoard.getContent().length());
        globalThrowError.ErrorNullBoardTitle(yBoard.getTitle());

        System.out.println("설명글 : "+yBoard.getDescription());


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

            // 해당 게시판의 썸네일 src값을 반환함
            yBoard.setThumbnail(thumbNailSrc(yBoard.getImagefileid()));

            yBoardRepository.save(yBoard);

            modifyImagefile(yBoard.getContent(),yBoard.getImagefileid());
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
                    // 해당 게시판의 썸네일 src값을 반환함
                    yBoard.setThumbnail(thumbNailSrc(yBoard.getImagefileid()));


                    yBoardRepository.save(yBoard);

                    modifyImagefile(yBoard.getContent(),yBoard.getImagefileid());
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
                    return new IllegalArgumentException("글 삭제 실패 아이디를 찾을 수 없습니다");
                });

        yReplyRepository.deleteAllByBoard(yBoard);

        // 관련된 사진파일을 삭제하기
        deleteConfirmfile(yBoard.getImagefileid());

        log.info("Delete USE from ID : " + id);
        yBoardRepository.deleteById(id);


    }

    @Transactional
    public int boardModify(YBoard yBoard) {

        if(scriptSearch(yBoard.getContent()) ||
                scriptSearch(yBoard.getTitle()) ||
                scriptSearch(yBoard.getDescription())){

            return -1; // 스크립트문 검색 발견
        }else{

            YBoard board = yBoardRepository.findById(yBoard.getId())
                    .orElseThrow(() -> {
                        return new IllegalArgumentException("글 찾기 실패 아이디를 찾을 수 없습니다");
                    }); // 영속화 시키기
            YBoard board1 = ImageSearch(board.getUser().getUsername(), yBoard, board,true);

            // 해당함수로  Service가 종료와 함게 트랜잭션 종료, 이때 더티체킹으로 자동 업데이트된다
            board.setTitle(yBoard.getTitle());
            board.setContent(board1.getContent());
            board.setDescription(yBoard.getDescription());

            // 이렇게 트랜잭션이 완료되고 여기서 yboard의  content를 받아서 현재 파일에 있는 이미지와 비교해서 없으면
            // 삭제하고 놔두는 로직을 짜야한다
            modifyImagefile(board.getContent(), board.getImagefileid());
            // 해당 게시판의 썸네일 src값을 반환함
            board.setThumbnail(thumbNailSrc(yBoard.getImagefileid()));
            return 1;

        }



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
        String temporary = null;
        if(AllStaticElement.OsName.equals("window")){
            temporary = "C:\\temporary_storage\\"+username; // 임시 파일 저장경로
        }else{
            temporary  ="/home/youseongjung/Templates/temporary_storage/"+username;
        }
        String fileRoot =null;
        String BoardTitleUUID =null;
        String chfileRoot =null;


        // 수정인걸 알았으니깐 이미 있는 파일 이름만 가져오면 됨
        if(modify){
            BoardTitleUUID = yBoard1.getImagefileid();
            if(AllStaticElement.OsName.equals("window")){
                fileRoot = "C:\\Confirm_SaveImage\\"+BoardTitleUUID+"\\";
            }else{
                fileRoot = "/home/youseongjung/Templates/Confirm_SaveImage/"+BoardTitleUUID+"/";
            }
            // 이건 컨텐츠 즉 db 저장될 애들의 값을 바꿔주기위해서 해놓음
            chfileRoot = "/Confirm_SaveImage/"+BoardTitleUUID+"/";
        }else{
            // 지금 새로만드는 파일이니깐 새로운 랜덤 키값을 지정
            BoardTitleUUID = UUID.randomUUID()+"-"+yBoard.getUser().getId();

            if(AllStaticElement.OsName.equals("window")){
                fileRoot = "C:\\Confirm_SaveImage\\"+BoardTitleUUID+"\\";	//저장확정 경로
            }else{
                fileRoot = "/home/youseongjung/Templates/Confirm_SaveImage/"+BoardTitleUUID+"/";
            }
            // 이건 컨텐츠 즉 db 저장될 애들의 값을 바꿔주기위해서 해놓음
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
                // 파일 이름이 UserName-"username"-"filename"
                // 이렇게 지정됨
                String SearchfileName ="UserName-"+username;

                if(f.isFile() && f.getName().startsWith(SearchfileName)) {
                    // 이름 변경 -> 파일 이동 -> 오리지널 파일로 이동 url 는 그럼?
                    // DB 이름도 변경해야함 Content 검사해서 변경해서 넣어주기
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

        String ChContent = yBoard.getContent().replace("/temporary_storage/"+username+"/", chfileRoot);


        yBoard.setContent(ChContent);
        yBoard.setImagefileid(BoardTitleUUID);


        return yBoard;
    }

    public void deletetemporaryStorage(String username, boolean writebool){
        String path = null;

        if(AllStaticElement.OsName.equals("window")){
            path = "C:\\temporary_storage\\"+username; // 임시 파일 저장경로
        }else{
            path  ="/home/youseongjung/Templates/temporary_storage/"+username;
        }


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
        String path = null;

        if(AllStaticElement.OsName.equals("window")){
            path = "C:\\Confirm_SaveImage\\"+filename; // 임시 파일 저장경로
        }else{
            path  ="/home/youseongjung/Templates/Confirm_SaveImage/"+filename;
        }

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
    public void modifyImagefile(String content, String imagefileid){
        String Confirm_SaveImage = null;

        if(AllStaticElement.OsName.equals("window")){
            Confirm_SaveImage = "C:\\Confirm_SaveImage\\"+imagefileid; // 임시 파일 저장경로
        }else{
            Confirm_SaveImage  ="/home/youseongjung/Templates/Confirm_SaveImage/"+imagefileid;
        }

        File dir = new File(Confirm_SaveImage);
        File[] files = dir.listFiles();

        if(files ==null){
            System.out.println("imageis Not add");
        }else {
            for (File f : files) {
                if(content.contains(f.getName())){
                    //문자가 포합되어있으니 수정된 파일이 있다는 뜻.
                }else{
                    f.delete();
                }
            }
        }

    }

    // 게시판 id 가져오기, 게시판 사진있는지 파악
    // 해당 경로파일
    public String thumbNailSrc(String imageFiled){
        String savecon;
        String con;

        if(AllStaticElement.OsName.equals("window")){
            con = "C:\\Confirm_SaveImage\\"+imageFiled+"\\";
            savecon = "\\Confirm_SaveImage\\"+imageFiled+"\\";
        }else{
            con = "/home/youseongjung/Templates/Confirm_SaveImage/"+imageFiled+"/";
            savecon = "/Confirm_SaveImage/"+imageFiled+"/";
        }

        File dir = new File(con);
        File[] files = dir.listFiles();

        //  값 자체가 비어있을때
        if(imageFiled.isEmpty()){
            return "\\img\\thumbnailandimage\\defaultthumbnail.png";
        }

        // 파일이 있어도 안에 파일은 하나도 없을수가 있음
        if(files ==null || files.length==0){
            // 아무 사진파일도 없다는 거니깐 썸네일 없음 기본 썸네일 해줘야함
            return "\\img\\thumbnailandimage\\defaultthumbnail.png";
        }else{
            // 한개라도 있다는 뜻 가장 첫번째 꺼를 반환
            //테스트 결과 확장자 명까지 가져옴 있는 그대로 가져옴
            return savecon+files[0].getName();
        }


    }


    //본문과 제목, 그리고 description에 스크립트문이 있는지 없는지 파악하기 위한 검색기
    public boolean scriptSearch(String content){
        String con = content.toLowerCase();
        if(con.contains("<script>") || con.contains("<iframe>")){
            return true;
        }
        else if(con.contains("&lt;script&gt;") || con.contains("&lt;iframe&gt;")){
            return true;
        }else {
            return false;
        }

    }




}






