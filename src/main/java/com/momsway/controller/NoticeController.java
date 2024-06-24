package com.momsway.controller;

import com.momsway.dto.NoticeDTO;
import com.momsway.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;


@Controller
@RequiredArgsConstructor
@Slf4j
public class NoticeController {
    private final NoticeService noticeService;
    @Value("D:\\uploadImg")
    private String saveFolder;

    @GetMapping( value="/getNotiImages/{filename}")
    public ResponseEntity<byte[]> getNoticeImage(@PathVariable String filename) {
        String fname = URLEncoder.encode(filename, StandardCharsets.UTF_8)
                .replace("+", "%20");
        InputStream in = null;
        ResponseEntity<byte[]> responseEntity;
        try {
            in = new FileInputStream(saveFolder + "/" + fname);
            HttpHeaders headers = new HttpHeaders();
            responseEntity = new ResponseEntity<>(FileCopyUtils.copyToByteArray(in)
                    ,headers , HttpStatus.OK);
        } catch(IOException e) {
            log.error("getImages error...{}",e);
            responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

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

    @GetMapping("/delnotice/{nid}")
    public ResponseEntity<String> delNotice(@PathVariable Long nid){
        int result = noticeService.delNotice(nid);
        String msg = "메롱";
        if(result==0){
            msg = "삭제가 실패하였습니다.";
        }else{
            msg = "삭제 되었습니다.";
        }
        return ResponseEntity.ok().body(msg);
    }

    @GetMapping("/insertNotice")
    public String insertNotice(Model model){
        model.addAttribute("insertAction","/insertNotice");
        model.addAttribute("noticeOnly","notice");
        return "boardinsert";
    }

    @PostMapping("/insertNotice")
    public String insertNoticeResult(@ModelAttribute NoticeDTO dto){
        System.out.println(dto.getCategory());
        System.out.println(dto.getNotify());
        for(int i=0; i<dto.getFiles().size(); i++){
            System.out.println(dto.getFiles().get(i).getOriginalFilename());
        }
        Long newNid = noticeService.insertNotice(dto,saveFolder);
        return "redirect:/notice/"+newNid;
    }

    @GetMapping("/modnotice/{nid}")
    public String modNotice(@PathVariable Long nid, Model model){
        NoticeDTO dto = noticeService.findByNid(nid);
        model.addAttribute("dto",dto);
        return "notice/noticeupdate";
    }
}
