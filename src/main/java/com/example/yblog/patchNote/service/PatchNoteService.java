package com.example.yblog.patchNote.service;

import com.example.yblog.config.auth.PrincipalDetail;
import com.example.yblog.model.PatchNote;
import com.example.yblog.repository.PatchNoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class PatchNoteService {

    @Autowired
    PatchNoteRepository patchNoteRepository;

    @Transactional(readOnly = true)
    public List<PatchNote> patchNotesList(Pageable pageable){
        Page<PatchNote> paging = patchNoteRepository.findAll(pageable);
        List<PatchNote> listPatchBoard = paging.getContent();

        return listPatchBoard;
    }
    @Transactional(readOnly = true)
    public Page<PatchNote> patchNotePage(Pageable pageable){
        return patchNoteRepository.findAll(pageable);
    }

    @Transactional
    public void pathNoteSave(PatchNote patchNote, PrincipalDetail principal){
        if(patchNote.getTitle() == null){
            System.out.println("do not Save because no Title");
        }else{
            patchNote.setUser(principal.getYUser());
            patchNoteRepository.save(patchNote);
        }


    }

    @Transactional(readOnly = true)
    public PatchNote detailsPatchNote(int id){
        return patchNoteRepository.findById(id)
                .orElseThrow(()->{
                    return new IllegalArgumentException("패치노트 글을 찾을수 없습니다");
                });
    }

    @Transactional
    public void patchNoteDelete(int patchnoteid){
        patchNoteRepository.deleteById(patchnoteid);
    }


    @Transactional
    public void patchNoteCountUp(int patchnoteId){
        PatchNote patchNote =  patchNoteRepository.findById(patchnoteId)
                .orElseThrow(()->{
                    return new IllegalArgumentException("패치노트 글을 찾을수 없습니다");
                });

        int countNum = patchNote.getCount();

        // 더티체킹
        patchNote.setCount(countNum+1);
    }


}
