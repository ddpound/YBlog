package com.example.yblog.admin.service;

import com.example.yblog.model.BoardReplyLimit;
import com.example.yblog.repository.BRLRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class Scheduler {

    @Autowired
    AdminService adminService;

    @Autowired
    BRLRepository brlRepository;

    @Scheduled(cron = "* * 12 12 12 *")
    public void croJobSch(){
        brlRepository.deleteAll();
        log.info("clear All Delete BoardReply Table");
    }

}
