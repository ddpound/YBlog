package com.example.yblog.admin.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CategoryController {

    //최신글, 카테고리별로 보기


    //카테고리 관리창
    @GetMapping(value = "/admin/category")
    public String categoryPage(){





        return "admin/boardCategory";
    }

    // 카테고리관리창의 요청
    // 추가, 수정, 삭제

    // 썸네일 추가




}
