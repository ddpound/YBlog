package com.example.yblog.main.service;


import com.example.yblog.model.SkillBoard;
import com.example.yblog.model.YBoard;
import com.example.yblog.repository.PatchNoteRepository;
import com.example.yblog.repository.PortfolioRepository;
import com.example.yblog.repository.SkillBoardRepository;
import com.example.yblog.repository.YBoardRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

// 모든 서비스 담당이라 모든 레파지토리 가져와야함
// 풀테이블 서치 할 예정 (데이터가 많지 않으니)
@Log4j2
@Service
public class MainService {

    @Autowired
    YBoardRepository yBoardRepository;

    @Autowired
    SkillBoardRepository skillBoardRepository;

    @Autowired
    PatchNoteRepository patchNoteRepository;

    @Autowired
    PortfolioRepository portfolioRepository;

    // 받아야하는거 (검색할 단어, 종류즉 카테고리)
    // 단어는 당연히 스트링이겠지?
    public  List listSearch(String searchWord ,
                            String boardCategory, Model model){

        //스트링 값을 받아와서 그대로 Enum 방식으로 담아준다 그값이 있얼을때만
        //실수로 다른 값들이 담기지 않기 위해 방지한것
        BoardCategy stBoardCategory = BoardCategy.valueOf(boardCategory);

        // ArrayList의 업캐스팅
        List boardList = new ArrayList();
        boolean allandnot = false;
        if(stBoardCategory == BoardCategy.allboard){
            allandnot = true;
        }

        switch (stBoardCategory){
            case allboard:
                log.info("Search allcategory");
            case yboard:

                model.addAttribute("yboardResult", yBoardRepository.searchBoardTitle(searchWord));
                log.info("Search yboard");
                if (stBoardCategory == BoardCategy.yboard){
                    break;
                }
            case skillboard:
                model.addAttribute("skillboardResult", skillBoardRepository.searchBoardTitle(searchWord));

                log.info("Search skillboard");
                if (stBoardCategory == BoardCategy.skillboard){
                    break;
                }
            case portfolio:
                model.addAttribute("portfolioboardResult", portfolioRepository.searchBoardTitle(searchWord));
                log.info("Search portfolio");
                if (stBoardCategory == BoardCategy.portfolio){
                    break;
                }
            case patchnote:
                model.addAttribute("patchNoteboardResult", patchNoteRepository.searchBoardTitle(searchWord));
                log.info("Search patchnote");
                if (stBoardCategory == BoardCategy.patchnote){
                    break;
                }


        }


        for(int i=0; i < boardList.size(); i++){
            System.out.println(boardList.get(i));
        }




        return boardList;
    }

    public enum BoardCategy{

        yboard, skillboard, portfolio, patchnote, allboard

    }



}
