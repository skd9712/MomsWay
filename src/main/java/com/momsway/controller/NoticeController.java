package com.momsway.controller;

import com.momsway.dto.NoticeDTO;
import com.momsway.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@Controller
@RequiredArgsConstructor
@Slf4j
public class NoticeController {
    private final NoticeService noticeService;
    @GetMapping("/notice")
    public String notice(Model model
            , @PageableDefault(size=2, sort = "nid", direction = Sort.Direction.ASC) Pageable pageable){
        List<NoticeDTO> toplist = noticeService.findTopList();
        Page<NoticeDTO> list = noticeService.findList(pageable);
        log.info("currPage... {}",pageable.getPageNumber());
        log.info("getTotalPage...{}",list.getTotalPages());
        log.info("totalElement...{}",list.getTotalElements());
        int pagePerBlock = 5;
        int startPage = (pageable.getPageNumber()/pagePerBlock)*pagePerBlock+1;
        int endPage = startPage+pagePerBlock-1;
        if(list.getTotalPages()<endPage)
            endPage = list.getTotalPages();
        log.info("startPage, endPage...{},{}",startPage,endPage);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);
        model.addAttribute("list",list);
        model.addAttribute("toplist",toplist);
        return "notice/notice";
    }

    @GetMapping("/notice/{nid}")
    public String noticeDetail(@PathVariable Long nid, Model model){
        NoticeDTO dto = noticeService.findByNid(nid);
        model.addAttribute("dto",dto);
        return "notice/noticedetail";
    }
}
