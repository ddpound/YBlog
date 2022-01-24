package com.example.yblog.admin.service;


import com.example.yblog.allstatic.AllStaticElement;
import com.example.yblog.model.BoardCategory;
import com.example.yblog.model.SkillBoard;
import com.example.yblog.repository.BoardCategoryRepository;
import com.example.yblog.repository.SkillBoardRepository;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {

    @Autowired
    BoardCategoryRepository boardCategoryRepository;

    @Autowired
    SkillBoardRepository skillBoardRepository;


    @Transactional(readOnly = true)
    public BoardCategory findbyIdBoardCategory(int id){
        return boardCategoryRepository.findById(id).
                orElseThrow(()->{
                    return new IllegalArgumentException("해당카테고리를 찾을수 없습니다");
                });
    }


    @Transactional(readOnly = true)
    public Page<BoardCategory> categoryList(Pageable pageable){

        return boardCategoryRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<BoardCategory> allCategoryList(){
        return boardCategoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public BoardCategory findOneBoardCategory(BoardCategory boardCategory){
        return boardCategoryRepository.findById(boardCategory.getId())
                .orElseThrow(()->{
                    return new IllegalArgumentException("해당 카테고리를 찾을수 없음");
                });
    }

    @Transactional(readOnly = true)
    public BoardCategory CategoryfindById(int cateId){
        return boardCategoryRepository.findById(cateId)
                .orElseThrow(()->{
                    return new IllegalArgumentException("해당 카테고리를 찾을수 없음");
                });
    }


    @Transactional
    public int SaveCategory(String catename , String thumnailfileRoot){
        BoardCategory boardCategory = new BoardCategory();


        boardCategory.setCategoryName(catename);
        boardCategory.setCategoryThunmbnail(thumnailfileRoot);

        boardCategoryRepository.save(boardCategory);


        return 1;
    }


    @Transactional
    public int modifyCategory(MultipartFile multipartFile, String categoryName, int id){

        BoardCategory boardCategory =  boardCategoryRepository.findById(id)
                .orElseThrow(()->{
                    return new IllegalArgumentException("찾는 카테고리가 없습니다");
                });



        String fileRoot = null;
        String fileRootmodifyDelete = null;
        if(AllStaticElement.OsName.equals("window")){
            // os를 파악해서 매번 반복하지않도록 만들자 (static에 필요사항임)
            fileRoot = "C:"+ File.separator+"Confirm_SaveImage\\thumbnail\\";	//저장될 외부 파일 경로
        }else{
            fileRoot = "/home/youseongjung/Templates/Confirm_SaveImage/thumbnail/"+File.separator;	//저장될 외부 파일 경로
        }


        String originalFileName = multipartFile.getOriginalFilename();
        String extension = originalFileName.substring(originalFileName.lastIndexOf(".")); //확장자 명 가져오기

        String savedFileName = "Thumbnail-"+ UUID.randomUUID()+ extension;

        File targetFile = new File(fileRoot+savedFileName);

        //여기서 전에 사용한 애들은 삭제하고저장해야함
        if(AllStaticElement.OsName.equals("window")){
            // os를 파악해서 매번 반복하지않도록 만들자 (static에 필요사항임)
            fileRootmodifyDelete = "C:";
        }else{
            fileRootmodifyDelete = "/home/youseongjung/Templates";
        }

        File delteFile = new File(fileRootmodifyDelete+boardCategory.getCategoryThunmbnail());


        try {
            if(delteFile.exists()){
                delteFile.delete();
            }
            InputStream fileStream = multipartFile.getInputStream();
            FileUtils.copyInputStreamToFile(fileStream, targetFile);


        }catch (IOException e){
            FileUtils.deleteQuietly(targetFile);
        }

        boardCategory.setCategoryName(categoryName);
        boardCategory.setCategoryThunmbnail("/Confirm_SaveImage/thumbnail/"+savedFileName);



        return 1;
    }



    //해당 카테고리를 지우려면 관련된 테이블 카테고리를 다 널로해야함
    // 연관관계를 끊으려면 이렇게해야함
    @Transactional
    public int deleteCategory(int id){
        BoardCategory boardCategory = boardCategoryRepository.findById(id)
                        .orElseThrow(()->{
                            return new IllegalArgumentException("글 삭제 실패 해당 카테고리가 없습니다");
                        });

        for (SkillBoard skillboard :skillBoardRepository.findByBoardCategory(boardCategory)
             ) {
            skillboard.setBoardCategory(null);
        }

        String fileRootmodifyDelete = null;
        //여기서 전에 사용한 애들은 삭제하고저장해야함
        if(AllStaticElement.OsName.equals("window")){
            // os를 파악해서 매번 반복하지않도록 만들자 (static에 필요사항임)
            fileRootmodifyDelete = "C:";
        }else{
            fileRootmodifyDelete = "/home/youseongjung/Templates";
        }

        File delteFile = new File(fileRootmodifyDelete+boardCategory.getCategoryThunmbnail());
        if(delteFile.exists()){
            delteFile.delete();
        }

        boardCategoryRepository.delete(boardCategory);

        return 1;
    }




}
