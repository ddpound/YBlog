package com.example.yblog.status;

import com.example.yblog.model.Status;
import com.example.yblog.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StatusService {

    //처음 게시물의 id는 1이다
    @Autowired
    StatusRepository statusRepository;

    // 저장, 수정, 조회

    @Transactional
    public void SaveStatus(Status status){
        statusRepository.save(status);
    }
    @Transactional
    public Status statusReturn(int id){
        return statusRepository.findById(id)
                .orElseThrow(()->{
                    return new IllegalArgumentException("상태 보기 실패 아이디를 찾을 수 없습니다");
                });
    }

    @Transactional
    public void ModifySatus(String newstatus){
        // 무조건 이것만 있을테니깐
        Status status = statusRepository.findById(1)
                .orElseThrow(()->{
                    return new IllegalArgumentException("상태 보기 실패 아이디를 찾을 수 없습니다");
                }); // 영속화 시키기
        status.setNowStatus(newstatus);
        // 더디체킹하기
    }




}
