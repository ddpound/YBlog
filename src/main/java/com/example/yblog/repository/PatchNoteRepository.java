package com.example.yblog.repository;

import com.example.yblog.model.PatchNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PatchNoteRepository extends JpaRepository<PatchNote,Integer> {

    @Query("SELECT id FROM PatchNote ")
    List<Integer> selectId();



}
