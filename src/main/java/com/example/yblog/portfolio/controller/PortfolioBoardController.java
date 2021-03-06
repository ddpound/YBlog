package com.example.yblog.portfolio.controller;


import com.example.yblog.allstatic.AllStaticElement;
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
import org.springframework.mobile.device.Device;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Controller
public class PortfolioBoardController {

    @Autowired
    PortfolioBoardService portfolioBoardService;

    Device device;

    @GetMapping(value = "/auth/portfolio")
    public String goPortfolio(Model model, @PageableDefault(size = 6,sort = "id",
            direction = Sort.Direction.DESC)Pageable pageable, HttpServletRequest request){

        model.addAttribute("boards",portfolioBoardService.portboardList(pageable));
        model.addAttribute("boardsPage",portfolioBoardService.portBoardListPage(pageable));



        return "PortfolioBoard/portfolioBoardMain";
    }

    @PostMapping(value = "/portboard/save")
    @ResponseBody
    public ResponseDto<Integer> fortboardSave(@RequestBody PortfolioBoard portfolioBoard, @AuthenticationPrincipal PrincipalDetail principal){

        if (principal.getUsername().equals(AllStaticElement.adminUser)){
            portfolioBoardService.portBoardSave(portfolioBoard,principal);
            System.out.println("Admin Y write portfolio board");
            return new ResponseDto<Integer>(HttpStatus.OK,1);
        }


        return new ResponseDto<Integer>(HttpStatus.INTERNAL_SERVER_ERROR,-1);
    }

    @GetMapping(value = "portboard/write")
    public String gowritePortfolio(@AuthenticationPrincipal PrincipalDetail principal ){

        if (principal.getUsername().equals(AllStaticElement.adminUser)){
            return "PortfolioBoard/portwrite";
        }else{
            return "redirect:/";
        }

    }
    @GetMapping(value = "/auth/portboard/details")
    public String portboardDetails(@RequestParam("id")int id, Model model, HttpServletRequest request,
                                   HttpServletResponse response){

        Cookie[] cookies = request.getCookies();
        int visit =0;
        if(cookies == null){
            Cookie cookie1 = new Cookie("visitport",request.getParameter("id"));
            cookie1.setMaxAge(1800);
            response.addCookie(cookie1);
            portfolioBoardService.BoardCountUp(id);
        }else{
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("visitport")){
                    visit =1;
                    if(cookie.getValue().contains(request.getParameter("id"))){
                        // ?????? ?????? ???????????? ???????????? ??????
                    }else{
                        cookie.setValue((cookie.getValue()+"_"+request.getParameter("id")));
                        cookie.setMaxAge(1800);
                        response.addCookie(cookie);
                        portfolioBoardService.BoardCountUp(id);
                    }

                }
            }
        }
        if(visit==0){
            Cookie cookie1 = new Cookie("visitport",request.getParameter("id"));
            cookie1.setMaxAge(1800);
            response.addCookie(cookie1);
            portfolioBoardService.BoardCountUp(id);
        }

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
        if (principal.getUsername().equals(AllStaticElement.adminUser)){
            portfolioBoardService.portBoardModify(portfolioBoard);

            return new ResponseDto<Integer>(HttpStatus.OK,1);
        }else {
            return new ResponseDto<Integer>(HttpStatus.INTERNAL_SERVER_ERROR,-1);
        }


    }

    @DeleteMapping(value = "/portboard/delete/{boardId}")
    @ResponseBody
    public  ResponseDto<Integer> portboardDelete(@PathVariable("boardId")int boardId, @AuthenticationPrincipal PrincipalDetail principal){
        if (principal.getUsername().equals(AllStaticElement.adminUser)){
            portfolioBoardService.portBoardDelete(boardId);
            return new ResponseDto<Integer>(HttpStatus.OK,1);
        }else{
            return new ResponseDto<Integer>(HttpStatus.INTERNAL_SERVER_ERROR,-1);
        }
    }

    @PostMapping(value="/portboard/temporarystorageImagefile", produces = "application/json")
    @ResponseBody
    public JsonObject temporarystorageImageupload(@RequestParam("file") MultipartFile multipartFile, @AuthenticationPrincipal PrincipalDetail principal) {

        if(principal.getUsername().equals(AllStaticElement.adminUser)){
            JsonObject jsonObject = new JsonObject();
            String fileRoot = null;

            if(AllStaticElement.OsName.equals("window")){
                // os??? ???????????? ?????? ????????????????????? ????????? (static??? ???????????????)
                fileRoot = "C:"+File.separator+"temporary_storage\\"+principal.getUsername()+"\\";	//????????? ?????? ?????? ??????
            }else{
                fileRoot = "/home/youseongjung/Templates/temporary_storage/"+principal.getUsername()+File.separator;	//????????? ?????? ?????? ??????
            }

            String originalFileName = multipartFile.getOriginalFilename();	//???????????? ?????????
            String extension = originalFileName.substring(originalFileName.lastIndexOf("."));	//?????? ?????????

            String savedFileName = "UserName-"+principal.getUsername()+"-"+ UUID.randomUUID() + extension;	//????????? ?????? ???

            File targetFile = new File(fileRoot + savedFileName);

            try {
                InputStream fileStream = multipartFile.getInputStream();
                FileUtils.copyInputStreamToFile(fileStream, targetFile);	//?????? ??????
                jsonObject.addProperty("url", "/temporary_storage/"+principal.getUsername()+"/"+savedFileName);
                jsonObject.addProperty("responseCode", "success");

            } catch (IOException e) {
                FileUtils.deleteQuietly(targetFile);	//????????? ?????? ??????
                jsonObject.addProperty("responseCode", "error");
                e.printStackTrace();

            }

            return jsonObject;
        }


        // ????????? ???????????? ????????? null?????? ??????
        return null;

    }


    @DeleteMapping(value = "/portboard/temporarydelete")
    public String temporaryDelete(@AuthenticationPrincipal PrincipalDetail principal){
        portfolioBoardService.deletetemporaryStorage(principal.getUsername(),false);
        return "redirect:/";
    }






}
