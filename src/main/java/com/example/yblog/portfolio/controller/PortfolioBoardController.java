package com.example.yblog.portfolio.controller;


import com.example.yblog.allstatic.IpHostName;
import com.example.yblog.config.auth.PrincipalDetail;
import com.example.yblog.dto.ResponseDto;
import com.example.yblog.model.PortfolioBoard;
import com.example.yblog.portfolio.service.PortfolioBoardService;
import com.google.gson.JsonObject;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Controller
public class PortfolioBoardController {

    @Autowired
    PortfolioBoardService portfolioBoardService;

    @GetMapping(value = "/auth/Portfolio")
    public String goPortfolio(Model model, @PageableDefault(sort = "id",
            direction = Sort.Direction.DESC)Pageable pageable){

        model.addAttribute("boards",portfolioBoardService.portboardList(pageable));
        model.addAttribute("boardsPage",portfolioBoardService.portBoardListPage(pageable));
        return "PortfolioBoard/PortfolioBoardDetail";
    }

    @PostMapping(value = "/portboard/save")
    @ResponseBody
    public ResponseDto<Integer> fortboardSave(@RequestBody PortfolioBoard portfolioBoard, @AuthenticationPrincipal PrincipalDetail principal){

        if (principal.getUsername().equals(IpHostName.adminUser)){
            portfolioBoardService.portBoardSave(portfolioBoard,principal);
            System.out.println("Admin Y write portfolio board");
            return new ResponseDto<Integer>(HttpStatus.OK,1);
        }


        return new ResponseDto<Integer>(HttpStatus.INTERNAL_SERVER_ERROR,-1);
    }

    @GetMapping(value = "portboard/write")
    public String gowritePortfolio(@AuthenticationPrincipal PrincipalDetail principal ){

        if (principal.getUsername().equals(IpHostName.adminUser)){
            return "PortfolioBoard/portwrite";
        }else{
            return "redirect:/";
        }

    }
    @GetMapping(value = "/auth/portboard/details")
    public String portboardDetails(@RequestParam("id")int id, Model model){
        model.addAttribute("portboard",portfolioBoardService.detailsPortPolio(id));

        return "PortfolioBoard/portboardDetails";
    }

    @GetMapping("/portboard/goPortBoardmodify/{boardId}")
    public String goPortModifyPage(@PathVariable("boardId")int boardId,Model model){
        model.addAttribute("portboard",portfolioBoardService.detailsPortPolio(boardId));

        return "PortfolioBoard/boardPortModifyDetails";
    }
    @PutMapping(value = "/portboard/modify")
    @ResponseBody
    public ResponseDto<Integer> portboardModify(@RequestBody PortfolioBoard portfolioBoard, @AuthenticationPrincipal PrincipalDetail principal){
        if (principal.getUsername().equals(IpHostName.adminUser)){
            portfolioBoardService.portBoardModify(portfolioBoard);

            return new ResponseDto<Integer>(HttpStatus.OK,1);
        }else {
            return new ResponseDto<Integer>(HttpStatus.INTERNAL_SERVER_ERROR,-1);
        }


    }

    @DeleteMapping(value = "/portboard/delete/{boardId}")
    @ResponseBody
    public  ResponseDto<Integer> portboardDelete(@PathVariable("boardId")int boardId, @AuthenticationPrincipal PrincipalDetail principal){
        if (principal.getUsername().equals(IpHostName.adminUser)){
            portfolioBoardService.portBoardDelete(boardId);
            return new ResponseDto<Integer>(HttpStatus.OK,1);
        }else{
            return new ResponseDto<Integer>(HttpStatus.INTERNAL_SERVER_ERROR,-1);
        }
    }

    @PostMapping(value="/portboard/temporarystorageImagefile", produces = "application/json")
    @ResponseBody
    public JsonObject temporarystorageImageupload(@RequestParam("file") MultipartFile multipartFile, @AuthenticationPrincipal PrincipalDetail principal) {

        if(principal.getUsername().equals(IpHostName.adminUser)){

        JsonObject jsonObject = new JsonObject();

        String fileRoot = "C:\\temporary_storage\\"+principal.getUsername()+"\\";	//저장될 외부 파일 경로
        String originalFileName = multipartFile.getOriginalFilename();	//오리지날 파일명
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));	//파일 확장자

        String savedFileName = "UserName-"+principal.getUsername()+"-"+ UUID.randomUUID() + extension;	//저장될 파일 명

        File targetFile = new File(fileRoot + savedFileName);

        try {
            InputStream fileStream = multipartFile.getInputStream();
            FileUtils.copyInputStreamToFile(fileStream, targetFile);	//파일 저장
            jsonObject.addProperty("url", "/temporary_storage/"+principal.getUsername()+"/"+savedFileName);
            jsonObject.addProperty("responseCode", "success");
        } catch (IOException e) {
            FileUtils.deleteQuietly(targetFile);	//저장된 파일 삭제
            jsonObject.addProperty("responseCode", "error");
            e.printStackTrace();
        }

        return jsonObject;
        }


        // 어드민 아이디가 아닐때 null값을 출력
        return null;

    }


    @DeleteMapping(value = "/portboard/temporarydelete")
    public String temporaryDelete(@AuthenticationPrincipal PrincipalDetail principal){
        portfolioBoardService.deletetemporaryStorage(principal.getUsername(),false);
        return "redirect:/";
    }






}
