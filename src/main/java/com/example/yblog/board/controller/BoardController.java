package com.example.yblog.board.controller;

import com.example.yblog.allstatic.IpHostName;
import com.example.yblog.board.service.BoardService;
import com.example.yblog.config.auth.PrincipalDetail;
import com.example.yblog.dto.ResponseDto;
import com.example.yblog.model.YBoard;
import com.example.yblog.model.YReply;
import com.example.yblog.status.StatusService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(value = "board")
public class BoardController {


    @Autowired
    BoardService boardService;

    @Autowired
    StatusService statusService;


    @GetMapping(value = "write")
    public String goWriteView(@AuthenticationPrincipal PrincipalDetail principal){
        // 이걸로 관리자계정 따로 만들어놔서 따로 통제 만들어놔야겠다
        System.out.println("Try to write user : "+ principal.getUsername());
        return "board/write";
    }

    @PostMapping(value = "save")
    @ResponseBody
    public ResponseDto<Integer> boardSave(@RequestBody YBoard yBoard, @AuthenticationPrincipal PrincipalDetail principal){
        // 4194304 최대길이 용량이 이럴때 에러가 발생
        // System.out.println("contentLengh "+ yBoard.getContent().length());

        //저장할때 유저네임을 이용해서 임시 저장이미지 저장파일을 영구 저장파일로 보내는로직을 짠 서비스를 만들면될듯
        // 유저이름, 제목

        boardService.SaveBoard(yBoard, principal.getYUser());

        System.out.println("Try to Save user : "+ principal.getUsername());

        return new ResponseDto<Integer>(HttpStatus.OK,1);
    }

    @DeleteMapping(value = "delete/{boardId}")
    @ResponseBody
    public ResponseDto<Integer> deleteBoard(@PathVariable("boardId") int id){
        boardService.delete(id);

        return new ResponseDto<Integer>(HttpStatus.OK,1);
    }

    @GetMapping(value = "gomodify/{boardId}")
    public String goModifyView(@PathVariable("boardId") int boardId, Model model, @AuthenticationPrincipal PrincipalDetail principal){
        YBoard insertYboard =  boardService.boardDetails(boardId);

        // 검색완료된 보드의 작성자 값과 현재 principal에 저장된 즉 현재 로그인중인 유저의 닉네임값이 같아야
        // 정리하면 현재 로그인 유저와 작성자가같아야 return 값을 올바르게 해준다
        // 관리자 계정은 언제든지  들어갈수 잇다
        if (insertYboard.getUser().getUsername().equals(principal.getUsername()) || principal.getUsername().equals("Y")){
            model.addAttribute("board", insertYboard);
            return "board/boardModifyDetails";
        }

        return "redirect:/";
    }

    @PutMapping("modify")
    @ResponseBody
    public ResponseDto<Integer> boardModify(@RequestBody YBoard board){

        boardService.boardModify(board);
        return new ResponseDto<Integer>(HttpStatus.OK,1);
    }

    // 여기는 수정글
    @GetMapping("changeStatus/{newstatus}")
    public String ChangeStatus(@PathVariable("newstatus") String newstatus,@AuthenticationPrincipal PrincipalDetail principal){
        if (principal.getUsername().equals(IpHostName.adminUser)){
            statusService.ModifySatus(newstatus);
        }

        return "redirect:/";
    }

    @PostMapping(value = "{boardId}/reply")
    @ResponseBody
    public ResponseDto<Integer> replySave(@PathVariable("boardId") int boardId, @RequestBody YReply yReply, @AuthenticationPrincipal PrincipalDetail principal){

        boardService.saveReply(principal.getYUser(), boardId, yReply);
        System.out.println("Try to SaveReply user : "+ principal.getUsername());

        return new ResponseDto<Integer>(HttpStatus.OK,1);
    }

    @DeleteMapping("{boardId}/reply/{replyId}")
    @ResponseBody
    public ResponseDto<Integer> replyDelte(@PathVariable int replyId,@AuthenticationPrincipal PrincipalDetail principal){
        YReply yReply = boardService.findYReply(replyId);

        if(yReply.getUser().getUsername().equals(principal.getUsername()) || principal.getUsername().equals(IpHostName.adminUser)){
            boardService.replyDelete(replyId);
            return new ResponseDto<Integer>(HttpStatus.OK,1);
        }

        // 어떤 사람이 강제적인 접근을 통해서 삭제를 시도하려함
        return new ResponseDto<Integer>(HttpStatus.INTERNAL_SERVER_ERROR,-1);
    }

    @PostMapping(value="/temporarystorageImagefile", produces = "application/json")
    @ResponseBody
    public JsonObject temporarystorageImageupload(@RequestParam("file") MultipartFile multipartFile,@AuthenticationPrincipal PrincipalDetail principal) {

        JsonObject jsonObject = new JsonObject();

        String fileRoot = "C:\\temporary_storage\\"+principal.getUsername()+"\\";	//저장될 외부 파일 경로
        String originalFileName = multipartFile.getOriginalFilename();	//오리지날 파일명
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));	//파일 확장자

        String savedFileName = "UserName-"+principal.getUsername()+"-"+UUID.randomUUID() + extension;	//저장될 파일 명

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


    @DeleteMapping(value = "temporarydelete")
    public String temporaryDelete(@AuthenticationPrincipal PrincipalDetail principal){
        boardService.deletetemporaryStorage(principal.getUsername(),false);
        return "redirect:/";
    }


}
