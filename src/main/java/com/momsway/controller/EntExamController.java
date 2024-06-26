package com.momsway.controller;

import com.momsway.domain.User;
import com.momsway.dto.EntExamDTO;
import com.momsway.dto.LikeDTO;
import com.momsway.dto.NoticeDTO;
import com.momsway.service.EntExamService;
import com.momsway.service.LikeService;
import com.momsway.service.NoticeService;
import com.momsway.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class EntExamController {


    @Value("D:\\backend\\upload_img")
    private String saveFolder;

    @Value("D:\\backend\\upload_img")
    private String filePath;
    private final LikeService likeService;
    private final EntExamService entExamService;
    private final NoticeService noticeService;
    private final UserService userService;

    @GetMapping("entexam")
    public String entexam(@PageableDefault(size = 10, page = 0) Pageable pageable, Model model
            , @RequestParam(required = false, defaultValue = "") String search_txt) {

        if(search_txt==null)
            search_txt = "";

        Page<EntExamDTO> entlist = entExamService.entlist(pageable, search_txt);
        List<NoticeDTO> toplist = noticeService.findTopList();
        int pagesize = 5;
        int startPage = ((int) (Math.ceil(pageable.getPageNumber() / pagesize))) * pagesize + 1;
        int endPage = Math.min(startPage + pagesize - 1, entlist.getTotalPages());
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("entlist", entlist);
        model.addAttribute("toplist", toplist);
        model.addAttribute("search_txt", search_txt);
        return "entexam/entexam";
    }

    @GetMapping("/insertent")
    public String insertEnt(Model model) {
        model.addAttribute("insertAction","/insertent");
        return "boardinsert";
    }

    @PostMapping("/insertent")
    public String entresult(EntExamDTO dto, Model model, Principal principal) {
        log.info("....upload file path: {}", saveFolder);
        String username = principal.getName();
        log.info("...username...{}",username);
        long uidByEmail = userService.findUidByEmail(username);
        log.info("uid,,,,{}",uidByEmail);
        long result = entExamService.upload(saveFolder, dto,username);
        log.info("...upload file length:{}", result);
        return "redirect:/entexam";
    }

    @GetMapping("/entdetail/{eid}")
    public String entexamDetail(@PathVariable Long eid, Model model) {
        EntExamDTO dto = entExamService.findByEid(eid);
        model.addAttribute("dto", dto);
        return "entexam/entexamdetail";
    }

    @GetMapping(value = "/getentimages/{filename}")
    public ResponseEntity<byte[]> getEntImage(@PathVariable String filename) {
        InputStream in = null;
        ResponseEntity<byte[]> entity = null;
        try {
            in = new FileInputStream(filePath + "/" + filename);
            HttpHeaders headers = new HttpHeaders();
            entity = new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(in)
                    , headers, HttpStatus.OK);
        } catch (IOException e) {
            System.out.println(e);
            entity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return entity;
    }
    @GetMapping("/entupdate/{eid}")
    public String entUpdate(@PathVariable Long eid, Model model){
        EntExamDTO dto = entExamService.findByEid(eid);
        model.addAttribute("dto",dto);
        return "entexam/entupdate";
    }
    @PostMapping("/entupdate/{eid}")
    public String entUpdateResult(@PathVariable Long eid, @ModelAttribute("dto") EntExamDTO dto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "entexam/entupdate";
        }else {
            long id =entExamService.entUpdate(eid,saveFolder, dto);
            return "redirect:/entexam";
        }
    }
    @GetMapping("/delentexam/{eid}")
    public ResponseEntity<String> delEnt(@PathVariable Long eid){
        int result = entExamService.delEnt(eid);
        String msg = "";
        if(result==0){
            msg = "삭제실패";
        }else {
            msg="삭제완료";
        }
        return ResponseEntity.ok().body(msg);
    }
    /*
        list (html 리턴)
        fetch용 list
        insert
        delete
        update
+
        insertlike
        deletelike

     */

    @GetMapping("/checklike")
    public ResponseEntity<String> checkLikeStatus(@RequestParam Long uid, @RequestParam Long eid,Principal principal) {
        String username = principal.getName();
        Long uidByEmail = userService.findUidByEmail(username);
        boolean liked = likeService.findLike(uidByEmail,eid,username);
        log.info("uid...{}",uidByEmail);
        log.info("eid, .{}",eid);

        return ResponseEntity.ok(liked+"");
    }
    @PostMapping("/insertlike")
    public @ResponseBody ResponseEntity<Integer> insertLike(@RequestBody LikeDTO dto,Principal principal) {
        String username = principal.getName();
        int result = 0;
        try {
            log.info("Received LikeDTO: {}", dto); // 요청 본문 확인
            likeService.insertLike(dto,username);
            result = 1;
        } catch (Exception e) {
            log.error("ExamController insertLike...{}", e);
        }
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/dellike/{lid}")
    public @ResponseBody ResponseEntity<Integer> delLike(@PathVariable Long lid) {
        int result = likeService.delLike(lid);
        return ResponseEntity.ok().body(result);
    }

}
