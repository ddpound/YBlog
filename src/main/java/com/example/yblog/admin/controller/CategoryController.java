package com.example.yblog.admin.controller;


import com.example.yblog.admin.service.CategoryService;
import com.example.yblog.allstatic.AllStaticElement;
import com.example.yblog.config.auth.PrincipalDetail;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

@Controller
public class CategoryController {

    @Value("${blogAdmin.adminName}")
    private  String adminName;

    @Autowired
    CategoryService categoryService;

    //최신글, 카테고리별로 보기

    //카테고리 관리창
    @GetMapping(value = "/admin/category")
    public String categoryPage(Model model,
                               @PageableDefault(size = 6, sort = "id" ,direction = Sort.Direction.DESC)
                               Pageable pageable,
                               HttpServletRequest request,
                               @AuthenticationPrincipal PrincipalDetail principal){

        if(principal.getUsername().equals(adminName)){
            model.addAttribute("categoryList", categoryService.categoryList(pageable));
            return "admin/boardCategory";
        }else{
            return "redirect:/";
        }

    }

    // 카테고리관리창의 요청
    // 추가, 수정, 삭제

    // 카테고리 추가;
    @PostMapping(value = "admin/category/save")
    public String UploadThumbnail(@RequestParam("file")MultipartFile multipartFile,
                                  @RequestParam("categoryName")String categoryName,
                                  @AuthenticationPrincipal PrincipalDetail principal){

        if(!principal.getUsername().equals(adminName)){
            return "redirect:/";
        }

        String fileRoot = null;
        if(AllStaticElement.OsName.equals("window")){
            // os를 파악해서 매번 반복하지않도록 만들자 (static에 필요사항임)
            fileRoot = "C:"+File.separator+"Confirm_SaveImage\\thumbnail\\";	//저장될 외부 파일 경로
        }else{
            fileRoot = "/home/youseongjung/Templates/Confirm_SaveImage/thumbnail/"+File.separator;	//저장될 외부 파일 경로
        }

        String originalFileName = multipartFile.getOriginalFilename();
        String extension = originalFileName.substring(originalFileName.lastIndexOf(".")); //확장자 명 가져오기

        String savedFileName = "Thumbnail-"+ UUID.randomUUID()+ extension;

        File targetFile = new File(fileRoot+savedFileName);


        try {
            InputStream fileStream = multipartFile.getInputStream();
            FileUtils.copyInputStreamToFile(fileStream, targetFile);
        }catch (IOException e){
            FileUtils.deleteQuietly(targetFile);
        }

        categoryService.SaveCategory(categoryName, "/Confirm_SaveImage/thumbnail/"+savedFileName);
        return "admin/boardCategory";
    }

    @PutMapping(value = "admin/category/modify")
    public String modifyCategory(@RequestParam("file")MultipartFile multipartFile,
                                 @RequestParam("categoryName")String categoryName,
                                 @RequestParam("categoryId")int categoryId,
                                 @AuthenticationPrincipal PrincipalDetail principal){

        if(!principal.getUsername().equals(adminName)){
            return "redirect:/";
        }


        categoryService.modifyCategory(multipartFile,categoryName,categoryId);


        return "admin/boardCategory";
    }

    @DeleteMapping(value = "admin/category/delete/{id}")
    public String categoryDelete(@AuthenticationPrincipal PrincipalDetail principal,
                                 @PathVariable(value = "id") int id){

        if(!principal.getUsername().equals(adminName)){
            return "redirect:/";
        }

        categoryService.deleteCategory(id);

        return "admin/boardCategory";
    }

}
