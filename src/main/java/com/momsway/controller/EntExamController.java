package com.momsway.controller;

import com.momsway.dto.EntExamDTO;
import com.momsway.dto.LikeDTO;
import com.momsway.service.EntExamService;
import com.momsway.service.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class EntExamController {
    private final LikeService likeService;
    private final EntExamService entExamService;
    @GetMapping("entexam")
    public String entexam(@PageableDefault(size = 10,page = 0) Pageable pageable, Model model){
        Page<EntExamDTO> entList = entExamService.entlist(pageable);
        int pagesize = 5;
        int startPage =((int)(Math.ceil(pageable.getPageNumber()/pagesize)))*pagesize+1;
        int endPage =Math.min(startPage+pagesize-1,entList.getTotalPages());
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);
        model.addAttribute("entList",entList);
        return "entexam/entexam";
    }
    @GetMapping("insertent")
    public String insertEnt(){
        return "entexam/insertent";

    }
    /*
        list (html 리턴)
        fetch용 list
        insert
        delete
        update

        insertlike
        deletelike

     */
    @PostMapping("/insertlike")
    public @ResponseBody ResponseEntity<Integer> insertLike(@RequestBody LikeDTO dto){
        int result = 0;
        try{
            likeService.insertLike(dto);
            result = 1;
        }catch (Exception e){
            log.error("ExamController insertLike...{}",e);
        }
        return ResponseEntity.ok().body(1);
    }

    @GetMapping("/dellike/{lid}")
    public @ResponseBody ResponseEntity<Integer> delLike(@PathVariable Long lid){
        int result = likeService.delLike(lid);
        return ResponseEntity.ok().body(result);
    }

}
