package com.example.yblog.repository;

import com.example.yblog.model.BanIp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BanIpRespository extends JpaRepository<BanIp,String> {

    Optional<BanIp> findById(String ip);

}
