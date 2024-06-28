package com.momsway.controller;

import com.momsway.dto.NoticeDTO;
import com.momsway.dto.UserDTO;
import com.momsway.service.NoticeService;
import com.momsway.service.UserService;
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
import org.springframework.security.core.context.SecurityContextHolder;
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
import java.util.ArrayList;
import java.util.List;


@Controller
@RequiredArgsConstructor
@Slf4j
public class NoticeController {
    private final NoticeService noticeService;
    private final UserService userService;

    @Value("D:\\uploadImg")
    private String saveFolder;


    @GetMapping( value="/getNotiImages/{filename}")
    public ResponseEntity<byte[]> getNoticeImage(@PathVariable String filename) {
        InputStream in = null;
        ResponseEntity<byte[]> responseEntity;
        try {
            in = new FileInputStream(saveFolder + "/" + filename);
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
            , @PageableDefault(size=10, sort = "nid", direction = Sort.Direction.ASC) Pageable pageable){
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
        noticeService.addNoticeReadNo(nid);
        NoticeDTO dto = noticeService.findByNid(nid);
        model.addAttribute("dto",dto);
        if(dto.getImgPaths()!=null)
            model.addAttribute("imgPaths",dto.getImgPaths());
        return "notice/noticedetail";
    }

    @GetMapping("/delnotice/{nid}")
    public ResponseEntity<String> delNotice(@PathVariable Long nid){
        int result = noticeService.delNotice(nid,saveFolder);
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
        model.addAttribute("top", "공지사항");
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

    @GetMapping("/updateNotice/{nid}")
    public String modNotice(@PathVariable Long nid, Model model){
        NoticeDTO dto = noticeService.findByNid(nid);
        model.addAttribute("dto",dto);
        List<String> imgs = new ArrayList<>();
        for(int i=0; i<dto.getImgPaths().size(); i++){
            imgs.add("/getNotiImages/"+dto.getImgPaths().get(i));
        }
        dto.setImgPaths(imgs);
        model.addAttribute("imgPaths",dto.getImgPaths());
        model.addAttribute("noticeOnly","notice");
        model.addAttribute("insertAction", "/updateNotice/"+nid);
        model.addAttribute("top", "공지사항");
        return "boardupdate";
    }

    @PostMapping("/updateNotice/{nid}")
    public String updateNoticeResult(@PathVariable Long nid, @ModelAttribute NoticeDTO dto){
//        for(String s: dto.getImgPaths()){
//            System.out.println(s);
//        }
        log.info("NoticeController getImgPaths(delete target)...{}",dto.getImgPaths());
        dto.setNid(nid);
        Long modNid = noticeService.updateNotice(dto,saveFolder);
        return "redirect:/notice/"+modNid;
    }
}
