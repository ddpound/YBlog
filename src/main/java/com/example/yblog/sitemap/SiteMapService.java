package com.example.yblog.sitemap;

import com.example.yblog.repository.PatchNoteRepository;
import com.example.yblog.repository.PortfolioRepository;
import com.example.yblog.repository.SkillBoardRepository;
import com.example.yblog.repository.YBoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;


@Service
public class SiteMapService {

    @Autowired
    PatchNoteRepository patchNoteRepository;

    @Autowired
    PortfolioRepository portfolioRepository;

    @Autowired
    YBoardRepository yBoardRepository;

    @Autowired
    SkillBoardRepository skillBoardRepository;


    Url url;

    UrlSet urlSet = new UrlSet();


    public UrlSet getUrlSet(){
        // 어레이 리스트 생성
        urlSet.setUrl(new ArrayList<>());
        // 설정
        urlSet.setXmlns("http://www.sitemaps.org/schemas/sitemap/0.9");

        // 기본이 되는 사이트들 설정
        defultSetUrlSet();

        //패치노트
        for (int id : patchNoteRepository.selectId()) {
            url = new Url();
            url.setLoc("https://www.ybloglab.shop/auth/patchnote/details?id="+id);
            url.setLastmod(new Date());
            urlSet.getUrl().add(url);
        }

        //보드메인
        for (int id : yBoardRepository.selectId()) {
            url = new Url();
            url.setLoc("https://www.ybloglab.shop/auth/board/details?id="+id);
            url.setLastmod(new Date());
            urlSet.getUrl().add(url);
        }


        //스킬보드
        for (int id : skillBoardRepository.selectId()) {
            url = new Url();
            url.setLoc("https://www.ybloglab.shop/auth/skillboard/details/"+id);
            url.setLastmod(new Date());
            urlSet.getUrl().add(url);
        }


        //포트폴리오
        for (int id : portfolioRepository.selectId()) {
            url = new Url();
            url.setLoc("https://www.ybloglab.shop/auth/portboard/details?id="+id);
            url.setLastmod(new Date());
            urlSet.getUrl().add(url);
        }



        return urlSet;
    }

    public void defultSetUrlSet(){
        url = new Url();
        url.setLoc("https://www.ybloglab.shop/");
        url.setLastmod(new Date());
        urlSet.getUrl().add(url);

        url = new Url();
        url.setLoc("https://www.ybloglab.shop/auth/patchnote/");
        url.setLastmod(new Date());
        urlSet.getUrl().add(url);

        url = new Url();
        url.setLoc("https://www.ybloglab.shop/auth/skillboard");
        url.setLastmod(new Date());
        urlSet.getUrl().add(url);

        url = new Url();
        url.setLoc("https://www.ybloglab.shop/auth/Portfolio");
        url.setLastmod(new Date());
        urlSet.getUrl().add(url);

        url = new Url();
        url.setLoc("https://www.ybloglab.shop/auth/boardmain");
        url.setLastmod(new Date());
        urlSet.getUrl().add(url);

    }


}
