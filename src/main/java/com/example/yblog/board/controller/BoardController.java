package com.example.yblog.board.controller;

import com.example.yblog.allstatic.IpHostName;
import com.example.yblog.board.service.BoardService;
import com.example.yblog.config.auth.PrincipalDetail;
import com.example.yblog.dto.ResponseDto;
import com.example.yblog.model.YBoard;
import com.example.yblog.model.YReply;
import com.example.yblog.status.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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



}
