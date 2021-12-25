package com.example.yblog.skillboard.service;

import com.example.yblog.allstatic.AllStaticElement;
import com.example.yblog.handler.GlobalThrowError;
import com.example.yblog.model.SkillBoard;
import com.example.yblog.model.SkillBoardReply;
import com.example.yblog.model.YBoard;
import com.example.yblog.model.YUser;
import com.example.yblog.repository.SkillBoardReplyRepository;
import com.example.yblog.repository.SkillBoardRepository;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class SkillBoardService {

    @Autowired
    private SkillBoardRepository skillBoardRepository;

    @Autowired
    private SkillBoardReplyRepository skillBoardReplyRepository;



    @Transactional
    // 사진이랑 같이 게시판 저장하기
    public int saveSkillBoard(SkillBoard skillBoard, YUser yUser){

        SkillBoard newSkillBoard;

        //사진 로직


        if(skillBoard.getTitle() ==null){
            System.out.println("do not Save because no title");
            return -1;
        }else{
            skillBoard.setCount(0);
            skillBoard.setUser(yUser);

            newSkillBoard = imageSearch(yUser.getUsername(),skillBoard,null,false);


            skillBoard.setContent(newSkillBoard.getContent());
            skillBoard.setImagefileid(newSkillBoard.getImagefileid());
            skillBoardRepository.save(skillBoard);

            modifyImagefile(skillBoard.getContent(), skillBoard.getImagefileid());
            deletetemporaryStorage(yUser.getUsername(),true);


            return 1;

        }


    }

    @Transactional(readOnly = true)
    public List<SkillBoard> skillBoardList(Pageable pageable){
        Page<SkillBoard> skillBoardsPaging = skillBoardRepository.findAll(pageable);
        List<SkillBoard> skillBoardList = skillBoardsPaging.getContent();

        return skillBoardList;
    }

    @Transactional(readOnly = true)
    public Page<SkillBoard> skillBoardPage(Pageable pageable){
        return  skillBoardRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public SkillBoard skillBoardDetails(int id){

        return skillBoardRepository.findById(id)
                .orElseThrow(()->{
                    return new IllegalArgumentException("글 상세보기 실패, 아이디를 찾을수없음");
                });
    }


    @Transactional
    public void skillBoardModify(SkillBoard skillBoard){

        SkillBoard board = skillBoardRepository.findById(skillBoard.getId())
                .orElseThrow(()->{
                    return new IllegalArgumentException("글 상세보기 실패, 아이디를 찾을수없음");
                });
        // 어째서인지 영속화를 진행하면 skillBoard의 값이 작동하지 않음

        // 위의 영속화된 애를 사용하면 문제없는듯
        SkillBoard skillBoard1 = imageSearch(board.getUser().getUsername(), skillBoard, board ,true);

        // 여기서 아주 중요한 에러를 찾아냄 영속화에 대한 개념부족인거같은데 아래와 같은 코드면 에러발생함
        // SkillBoard skillBoard1 = imageSearch(skillBoard.getUser().getUsername(), skillBoard, board ,true);

        //더티체킹
        board.setTitle(skillBoard.getTitle());
        board.setContent(skillBoard1.getContent());
        board.setDescription(skillBoard.getDescription());

        modifyImagefile(board.getContent(), board.getImagefileid());


    }

    @Transactional
    public void deleteSkillBoard(int id){
        SkillBoard skillBoard = skillBoardRepository.findById(id)
                .orElseThrow(() -> {
                    return new IllegalArgumentException("글 삭제 실패 아이디를 찾을 수 없습니다");
                });

        //먼저 이보드와 관련된 댓글들 전부 삭제
        skillBoardReplyRepository.deleteAllByBoard(skillBoard);

        // 해당 파일의 경로명으로 따라가 파일들 전부 삭제
        deleteConfirmfile(skillBoard.getImagefileid());

        skillBoardRepository.deleteById(id);

    }


    // 이미지를 검색하고, 게시판에 저장된 이미지 값을 변경하고
    public SkillBoard imageSearch(String username, SkillBoard skillBoard, SkillBoard skillBoard1, boolean modify){
        String temporary = null;



        // 접속 os에 따른 저장 경로 변경
        if(AllStaticElement.OsName.equals("window")){
            temporary = "C:\\temporary_storage\\"+username; // 임시 파일 저장경로
        }else{
            temporary  ="/home/youseongjung/Templates/temporary_storage/"+username;
        }

        String fileRoot =null;
        String BoardTitleUUID =null;
        String chfileRoot =null;

        if(modify){
            BoardTitleUUID = skillBoard1.getImagefileid();
            if(AllStaticElement.OsName.equals("window")){
                fileRoot = "C:\\Confirm_SaveImage\\"+BoardTitleUUID+"\\";
            }else{
                fileRoot = "/home/youseongjung/Templates/Confirm_SaveImage/"+BoardTitleUUID+"/";
            }
            // 이건 컨텐츠 즉 db 저장될 애들의 값을 바꿔주기위해서 해놓음
            chfileRoot = "/Confirm_SaveImage/"+BoardTitleUUID+"/";
        }else {
            // 지금 새로만드는 파일이니깐 새로운 랜덤 키값을 지정
            BoardTitleUUID = UUID.randomUUID()+"-"+skillBoard.getUser().getId();

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
            // 왜냐면 나는 관리자인데 악의적인 10장이상의 사진을 올리지 않기때문에
            /*if(files.length >10){
                return null;
            }*/

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
        String ChContent = skillBoard.getContent().replace("/temporary_storage/"+username+"/", chfileRoot);


        skillBoard.setContent(ChContent);
        skillBoard.setImagefileid(BoardTitleUUID);


        return skillBoard;
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


    @Transactional
    public void boardCountUp(int skillboardId) {
        SkillBoard board = skillBoardRepository.findById(skillboardId)
                .orElseThrow(() -> {
                    return new IllegalArgumentException("글 찾기 실패 아이디를 찾을 수 없습니다");
                }); // 영속화 시키기

        int countNum = board.getCount();

        board.setCount(countNum+1);


        // 해당함수로  Service가 종료와 함게 트랜잭션 종료, 이때 더티체킹으로 자동 업데이트된다

    }
}
