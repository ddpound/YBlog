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
        portfolioBoard.setUser(principal.getYUser());

        portfolioRepository.save(portfolioBoard);

    }






}
