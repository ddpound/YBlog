package com.example.yblog.skillboard.controller;

import com.example.yblog.allstatic.AllStaticElement;
import com.example.yblog.config.auth.PrincipalDetail;
import com.example.yblog.dto.ResponseDto;
import com.example.yblog.model.SkillBoard;
import com.example.yblog.skillboard.service.SkillBoardService;
import com.google.gson.JsonObject;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;


// REST API 구조 형식으로짜는게 나을듯
//  자바 스크립트 단에서 요청하는걸로
@Controller
@RequestMapping(value = "skillboard")
public class SkillBoardController {

    @Autowired
    SkillBoardService skillBoardService;


    @GetMapping(value = "writepage")
    public String skillBoardWritePage(){

        return "skillBoard/skillBoardWrite";
    }

    // 글쓰기
    @PostMapping(value = "skillboardwrite")
    @ResponseBody
    public ResponseDto<Integer> skillWrite(@RequestBody SkillBoard skillBoard,
                                           @AuthenticationPrincipal PrincipalDetail principalDetail){


        System.out.println(skillBoard.getTitle());
        skillBoardService.saveSkillBoard(skillBoard,principalDetail.getYUser());

        return new ResponseDto<Integer>(HttpStatus.OK,1);
    }

    // 글 올릴때 사진 비동기로 로컬 파일에 올려줌

    @PostMapping(value = "temporarystorageImagefile", produces = "application/json")
    @ResponseBody
    public JsonObject boardUploadPiture(
            @RequestParam("file")MultipartFile multipartFile,
            @AuthenticationPrincipal PrincipalDetail principal
            ){

        JsonObject jsonObject = new JsonObject();
        String fileRoot = null;
        if(AllStaticElement.OsName.equals("window")){
            // os를 파악해서 매번 반복하지않도록 만들자 (static에 필요사항임)
            fileRoot = "C:"+ File.separator+"temporary_storage\\"+principal.getUsername()+"\\";	//저장될 외부 파일 경로
        }else{
            fileRoot = "/home/youseongjung/Templates/temporary_storage/"+principal.getUsername()+File.separator;	//저장될 외부 파일 경로
        }

        //아래꺼가 기본 윈도우 경로
        //String fileRoot = "C:\\temporary_storage\\"+principal.getUsername()+"\\";	//저장될 외부 파일 경로
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

    @DeleteMapping("temporarydelete")
    public String skillTemporaryDelete(@AuthenticationPrincipal PrincipalDetail principal){
        skillBoardService.deletetemporaryStorage(principal.getUsername(), false);
        return "redirect:/";
    }


}
