package com.example.yblog.portfolio.service;

import com.example.yblog.config.auth.PrincipalDetail;
import com.example.yblog.model.PortfolioBoard;
import com.example.yblog.repository.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
            portfolioBoard.setUser(principal.getYUser());
            portfolioRepository.save(portfolioBoard);
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
        eternalPortfolioBoard.setTitle(portfolioBoard.getTitle());
        eternalPortfolioBoard.setContent(portfolioBoard.getContent());
        // 더티체킹

    }

    @Transactional
    public  void portBoardDelete(int portboardId){
        portfolioRepository.deleteById(portboardId);
    }











}
