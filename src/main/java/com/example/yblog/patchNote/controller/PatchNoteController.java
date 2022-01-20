package com.example.yblog.patchNote.controller;


import com.example.yblog.allstatic.AllStaticElement;
import com.example.yblog.config.auth.PrincipalDetail;
import com.example.yblog.dto.ResponseDto;
import com.example.yblog.model.PatchNote;
import com.example.yblog.patchNote.service.PatchNoteService;
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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class PatchNoteController {

    @Autowired
    PatchNoteService patchNoteService;

    Device device;

    @GetMapping(value = "/auth/patchnote")
    public String goPatchNoteMain(Model model, @PageableDefault(size=5, sort = "id",
    direction = Sort.Direction.DESC)Pageable pageable,HttpServletRequest request){


        model.addAttribute("boards", patchNoteService.patchNotesList(pageable));
        model.addAttribute("boardsPage",patchNoteService.patchNotePage(pageable));


        return "PatchNoteBoard/patchNoteMain";
    }


    @GetMapping(value = "/patchnote/gowrite")
    public String goPatchWrite(@AuthenticationPrincipal PrincipalDetail principal){
        if (principal.getUsername().equals(AllStaticElement.adminUser)){
            return "PatchNoteBoard/patchWrite";
        }else {
            return "redirect:/";
        }
    }

    @PostMapping(value = "/patchnote/write")
    @ResponseBody
    public ResponseDto<Integer> patchNoteSave(@RequestBody PatchNote patchNote,
                                              @AuthenticationPrincipal PrincipalDetail principal){

        if (principal.getUsername().equals(AllStaticElement.adminUser)){
            patchNoteService.pathNoteSave(patchNote,principal);
            return new ResponseDto<Integer>(HttpStatus.OK,1);
        }else{
            patchNoteService.pathNoteSave(patchNote,principal);
            return new ResponseDto<Integer>(HttpStatus.INTERNAL_SERVER_ERROR,-1);
        }

    }

    @GetMapping(value = "/auth/patchnote/details")
    public String patchNoteDetails(@RequestParam("id")int id, Model model,
                                   HttpServletRequest request,
                                   HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        int visit =0;
        if(cookies == null){
            Cookie cookie1 = new Cookie("patchvisit",request.getParameter("id"));
            cookie1.setMaxAge(60*60*24);
            response.addCookie(cookie1);
            patchNoteService.patchNoteCountUp(id);
        }else{
            for(Cookie cookie : cookies){
                if(cookie.getName().equals("patchvisit")){
                    visit =1;
                    if(cookie.getValue().contains(request.getParameter("id"))){
                        // 이미 있는 쿠키라서 아무것도 안함
                    }else{
                        cookie.setValue((cookie.getValue()+"_"+request.getParameter("id")));
                        cookie.setMaxAge(60*60*24);
                        response.addCookie(cookie);
                        patchNoteService.patchNoteCountUp(id);
                    }
                }
            }
        }  if(visit==0){
            Cookie cookie1 = new Cookie("patchvisit",request.getParameter("id"));
            cookie1.setMaxAge(60*60*24);
            response.addCookie(cookie1);
            patchNoteService.patchNoteCountUp(id);
        }


        model.addAttribute("patchboard", patchNoteService.detailsPatchNote(id));

        return "PatchNoteBoard/patchNoteDetails";
    }

    @DeleteMapping(value = "/patchboard/delete/{boardId}")
    @ResponseBody
    public ResponseDto<Integer> DeletePatchBoard(@PathVariable("boardId")int boardId
    , @AuthenticationPrincipal PrincipalDetail principal){

        if(principal.getUsername().equals(AllStaticElement.adminUser)){
            patchNoteService.patchNoteDelete(boardId);
            return new ResponseDto<Integer>(HttpStatus.OK,1);
        }

        return new ResponseDto<Integer>(HttpStatus.INTERNAL_SERVER_ERROR,-1);
    }


}
