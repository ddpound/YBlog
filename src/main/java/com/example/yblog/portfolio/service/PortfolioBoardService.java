package com.example.yblog.portfolio.service;

import com.example.yblog.config.auth.PrincipalDetail;
import com.example.yblog.model.PortfolioBoard;
import com.example.yblog.model.YBoard;
import com.example.yblog.repository.PortfolioRepository;
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
import java.util.Optional;
import java.util.UUID;

@Service
public class PortfolioBoardService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Transactional(readOnly = true)
    public List<PortfolioBoard> portboardList(Pageable pageable){
        Page<PortfolioBoard> paging = portfolioRepository.findAll(pageable);
        List<PortfolioBoard> listBoard = paging.getContent();
        return listBoard;
    }

    @Transactional(readOnly = true)
    public Page<PortfolioBoard> portBoardListPage(Pageable pageable){
        return  portfolioRepository.findAll(pageable);
    }

    @Transactional
    public void portBoardSave(PortfolioBoard portfolioBoard , PrincipalDetail principal){
        if(portfolioBoard.getTitle() != null){
            PortfolioBoard portfolioBoard1 = new PortfolioBoard();

            portfolioBoard.setUser(principal.getYUser());

            portfolioBoard1 = ImageSearch(principal.getUsername(), portfolioBoard,null,  false);

            portfolioBoard.setContent(portfolioBoard1.getContent());
            portfolioBoard.setImagefileid(portfolioBoard1.getImagefileid());


            portfolioRepository.save(portfolioBoard);


            deletetemporaryStorage(principal.getUsername(), true);

        }else{
            System.out.println("do not Save because no title");
        }


    }

    @Transactional(readOnly = true)
    public  PortfolioBoard detailsPortPolio(int id){
        return portfolioRepository.findById(id)
                .orElseThrow(()->{
                    return new IllegalArgumentException("포트폴리오 글 상세보기 실패, 아이디를 찾을수 없음");
                });
    }

    @Transactional
    public void portBoardModify(PortfolioBoard portfolioBoard){
        PortfolioBoard eternalPortfolioBoard = portfolioRepository.findById(portfolioBoard.getId())
                .orElseThrow(()->{
                    return new IllegalArgumentException("포트폴리오 게시판 수정실패 아이디를 찾을수 없습니다.");
                });

        // 글과 사진 수정, 추가
        PortfolioBoard portfolioBoard1 = ImageSearch(eternalPortfolioBoard.getUser().getUsername(),
                portfolioBoard,eternalPortfolioBoard,true);

        eternalPortfolioBoard.setTitle(portfolioBoard.getTitle());
        eternalPortfolioBoard.setContent(portfolioBoard1.getContent());
        // 더티체킹

    }

    @Transactional
    public  void portBoardDelete(int portboardId){
        Optional<PortfolioBoard> portfolioBoard = portfolioRepository.findById(portboardId);

        if (portfolioBoard.isEmpty()){
            System.out.println("portboard id null");
        }else{
            deleteConfirmfile(portfolioBoard.get().getImagefileid());
            portfolioRepository.deleteById(portboardId);
        }
    }


    // 임시 파일에있던 사진들을 검색 -> 이름변환 옮겨저장함함

    // 1. 파일들을 이름 변경해서 본파일로 옮김
    // 2. contents 안에있는 사진 src값을 변경 (board를)
    // 3. 임시파일 사진들을 전부 삭제
    // modify => 수정이면 참, 거짓이면 새로생성
    public PortfolioBoard ImageSearch(String username , PortfolioBoard portfolioBoard, PortfolioBoard yBoard1, boolean modify)  {
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
            BoardTitleUUID = UUID.randomUUID()+"-"+portfolioBoard.getUser().getId();
            fileRoot = "C:\\Confirm_SaveImage\\"+BoardTitleUUID+"\\";	//저장확정 경로
            chfileRoot = "/Confirm_SaveImage/"+BoardTitleUUID+"/";
        }

        File dir = new File(temporary);
        File[] files = dir.listFiles();
        if(files ==null){
            System.out.println("imageis Not add");
        }else{
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

                    System.out.println("파일 이름체크");

                }

            }
        }


        System.out.println(portfolioBoard.getContent());
        String ChContent = portfolioBoard.getContent().replace("/temporary_storage/"+username+"/", chfileRoot);
        System.out.println(ChContent);

        portfolioBoard.setContent(ChContent);
        portfolioBoard.setImagefileid(BoardTitleUUID);

        return portfolioBoard;
    }


    public void deletetemporaryStorage(String username, boolean writebool){
        String path = "C:\\temporary_storage\\"+username;
        File folder = new File(path);
        try {
            while(folder.exists()) {
                File[] folder_list = folder.listFiles(); //파일리스트 얻어오기

                for (int j = 0; j < folder_list.length; j++) {
                    folder_list[j].delete(); //파일 삭제
                    System.out.println("All delete Temporary File UserName : "+username);

                }

                if(folder_list.length == 0 && folder.isDirectory()){
                    folder.delete(); //대상폴더 삭제
                    System.out.println("All delete Temporary Folder UserName : "+username);
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
    public void BoardCountUp(int portfolioBoardId){
        PortfolioBoard eternalPortfolioBoard = portfolioRepository.findById(portfolioBoardId)
                .orElseThrow(()->{
                    return new IllegalArgumentException("포트폴리오 게시판 수정실패 아이디를 찾을수 없습니다.");
                });

        int countNum = eternalPortfolioBoard.getCount()+1;
        eternalPortfolioBoard.setCount(countNum);


        // 더티체킹

    }







}
