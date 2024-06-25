package com.momsway.controller;

import com.momsway.dto.AcademyDTO;
import com.momsway.dto.NoticeDTO;
import com.momsway.service.AcademyService;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.*;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AcademyController {
    private final AcademyService academyService;
    private final NoticeService noticeService;
    private final UserService userService;

//    private long uid = 0;
//
//    private void uidInit(String sessionId){
//        log.info("uidInit sessionId... {}",sessionId);
//        if(!sessionId.equals("anonymousUser"))
//            uid = userService.findUidByEmail(sessionId);
//    }

    @Value("D:\\uploadImg")
    private String saveFolder;

    @GetMapping( value="/getAcaImages/{filename}")
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

    @GetMapping("/academy")
    public String academy(Model model
            , @PageableDefault(size=2, sort = "aid", direction = Sort.Direction.ASC) Pageable pageable
            , @RequestParam(required = false, defaultValue = "") String search_txt){

        List<NoticeDTO> toplist = noticeService.findTopList();
        Page<AcademyDTO> list = academyService.findAcademyList(pageable, search_txt);
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
        model.addAttribute("search_txt",search_txt);
        return "academy/academy";
    }

    @GetMapping("/academy/{aid}")
    public String detail(@PathVariable Long aid, Model model){
        AcademyDTO dto = academyService.findByAid(aid);
        if(dto.getImgPath()!=null) {
            String[] imgPaths = dto.getImgPath().split(";-;");
            model.addAttribute("imgPaths",imgPaths);
        }
        model.addAttribute("dto",dto);
        return "academy/academydetail";
    }

    @GetMapping("/delacademy/{aid}")
    public ResponseEntity<String> delAcademy(@PathVariable Long aid){
        int result = academyService.delAcademy(aid,saveFolder);
        String msg = "메롱";
        if(result==0){
            msg = "삭제가 실패하였습니다.";
        }else{
            msg = "삭제 되었습니다.";
        }
        return ResponseEntity.ok().body(msg);
    }

    @GetMapping("/insertAcademy")
    public String insertAcademy(Model model){
        model.addAttribute("insertAction","/insertAcademy");
        return "boardinsert";
    }

    @PostMapping("/insertAcademy")
    public String insertAcademyResult(@ModelAttribute AcademyDTO dto){
        for(int i=0; i<dto.getFiles().size(); i++){
            System.out.println(dto.getFiles().get(i).getOriginalFilename());
        }
        Long newAid = academyService.insertAcademy(dto,saveFolder);
        return "redirect:/academy/"+newAid;
    }

    @GetMapping("/updateAcademy/{aid}")
    public String updateAcademy(@PathVariable Long aid, Model model){
        AcademyDTO dto = academyService.findByAid(aid);
        if(dto.getImgPath()!=null) {
            String[] imgPaths = dto.getImgPath().split(";-;");
            for(int i=0; i<imgPaths.length; i++){
                imgPaths[i] = "/getAcaImages/"+imgPaths[i];
            }
            model.addAttribute("imgPaths",imgPaths);
        }
        model.addAttribute("dto",dto);
        model.addAttribute("insertAction", "/updateAcademy");
        return "boardupdate";
    }
}
