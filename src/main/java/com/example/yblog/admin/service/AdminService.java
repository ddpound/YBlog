package com.example.yblog.admin.service;

import com.example.yblog.model.BanIp;
import com.example.yblog.model.BoardReplyLimit;
import com.example.yblog.model.YUser;
import com.example.yblog.repository.BRLRepository;
import com.example.yblog.repository.BanIpRespository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Log4j2
@Service
public class AdminService {

    @Autowired
    BanIpRespository banIpRespository;

    @Autowired
    BRLRepository brlRepository;


    // 밴당한 아이피가 있을때 참을 출력
    public boolean findIp(String ip){
        // 널일수도 있는 객체
        Optional<BanIp> resultIp = banIpRespository.findById(ip);

        if(resultIp.isEmpty()){
            return false;
        }
        return true;
    }

    public void saveIp(BanIp banIp){
        banIpRespository.save(banIp);
    }

    public void deleteIp(String ip){
        banIpRespository.deleteById(ip);
    }

    public void IpdeleteAll(){
        banIpRespository.deleteAll();
    }

    @Transactional
    public void FirstSaveLimitTable(YUser yUser){
        BoardReplyLimit boardReplyLimit = new BoardReplyLimit();
        boardReplyLimit.setUser(yUser);
        brlRepository.save(boardReplyLimit);
    }

    @Transactional(readOnly = true)
    public BoardReplyLimit findbrlRepositoryUser(YUser yUser){
        Optional<BoardReplyLimit> optionalBoardReplyLimit = brlRepository.findByUser(yUser);

        if(optionalBoardReplyLimit.isEmpty()){
            return null;
        }

        return optionalBoardReplyLimit.get();
    }

    @Transactional
    public void BRLimitboardADD(YUser yUser){
        Optional<BoardReplyLimit> boardReplyLimit = brlRepository.findByUser(yUser);

        if(boardReplyLimit.isEmpty()){
            log.info("not found table User");

        }else{
            int localBoardCount = boardReplyLimit.get().getBoardCount();
            boardReplyLimit.get().setBoardCount(localBoardCount+1);
            // 더티체킹
        }

    }

    @Transactional
    public void BRLimitReplyADD(YUser yUser){
        Optional<BoardReplyLimit> boardReplyLimit = brlRepository.findByUser(yUser);

        if(boardReplyLimit.isEmpty()){
            log.info("not found table User");

        }else{
            int localReplyCount = boardReplyLimit.get().getReplyCount();
            boardReplyLimit.get().setReplyCount(localReplyCount+1);
            // 더티체킹
        }

    }

}
