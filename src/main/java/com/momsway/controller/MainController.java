package com.momsway.controller;

import com.momsway.dto.AcademyDTO;
import com.momsway.dto.EntExamDTO;
import com.momsway.dto.NoticeDTO;
import com.momsway.service.MainService;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MainController {

    private final MainService mainService;

    @GetMapping("/main")
    public String main(Model model){

        /* 입시 인기글(좋아요순) 5개 */
        List<Object[]> entLikeList = mainService.entExamLikeSortList();

//        List<Long> countLike = new ArrayList<>();
//        for(Object[] o: entLikeList){
//            countLike.add((Long) o[2]);
//        }

        /* 공지사항 최신순 5개 */
        List<NoticeDTO> noticeList = mainService.noticeLatestList();

        /* 학원홍보 최신순 5개 */
        List<AcademyDTO> academyList = mainService.academyLatestList();

        /* 입시 최신순 5개 */
        List<EntExamDTO> entLatestList = mainService.entExamLatestList();

        model.addAttribute("entLikeList", entLikeList);
        model.addAttribute("noticeList", noticeList);
        model.addAttribute("academyList", academyList);
        model.addAttribute("entLatestList", entLatestList);

        return "main";
    }


//    @GetMapping("/search")
//    public String searchList(@RequestParam(required = false, defaultValue = "") String search
//            , @RequestParam(required = false, defaultValue = "") String search_txt){
//
//        if(search_txt==null)
//            search_txt="";
//
//        if("EntExam".equals(search)){
//            mainService.searchEntExam(search, search_txt);
//            return "redirect:/entexam";
//        }else if("Academy".equals(search)){
//            mainService.searchAcademy(search, search_txt);
//            return "redirect:/academy";
//        }else{
//            return "redirect:/main";
//        }
//    }
}
