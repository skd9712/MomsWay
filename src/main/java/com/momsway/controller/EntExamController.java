package com.momsway.controller;

import com.momsway.dto.LikeDTO;
import com.momsway.service.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class EntExamController {
    private final LikeService likeService;
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
