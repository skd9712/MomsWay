package com.momsway.controller;

import com.momsway.dto.AcademyDTO;
import com.momsway.dto.NoticeDTO;
import com.momsway.exception.CustomException;
import com.momsway.service.AcademyService;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.util.FileCopyUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.*;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@Validated
public class AcademyController {
    private final AcademyService academyService;
    private final NoticeService noticeService;

    @Value("${spring.servlet.multipart.location}")
    private String saveFolder;

    @GetMapping("/getAcaImages/{filename}")
    public ResponseEntity<byte[]> getNoticeImage(@PathVariable String filename) {
        InputStream in = null;
        ResponseEntity<byte[]> responseEntity;
        HttpHeaders headers = new HttpHeaders();
        try {
            in = new FileInputStream(saveFolder + "/" + filename);
            responseEntity = new ResponseEntity<>(FileCopyUtils.copyToByteArray(in)
                    ,headers , HttpStatus.OK);
        } catch(IOException e) {
            log.info("getAcaImages error...{}",e);
            try{
                in = new FileInputStream("src/main/resources/static/images/noimg.png");
                responseEntity = new ResponseEntity<>(FileCopyUtils.copyToByteArray(in)
                        , headers, HttpStatus.OK);
            }catch (IOException e2) {
                log.error("getAcaImages error...{}",e2);
                responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        return responseEntity;
    }

    @GetMapping("/academy")
    public String academy(Model model
            , @PageableDefault(size=10, sort = "aid", direction = Sort.Direction.ASC) Pageable pageable
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        academyService.addAcademyReadNo(aid, auth.getName());
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
        AcademyDTO dto = academyService.findByAid(aid);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth.getName().equals(dto.getEmail())
                || auth.getAuthorities().toString().equals("[ROLE_ADMIN]")){
            int result = academyService.delAcademy(aid,saveFolder);
            String msg = "메롱";
            if(result==0)
                msg = "삭제가 실패하였습니다.";
            else
                msg = "삭제 되었습니다.";
            return ResponseEntity.ok().body(msg);
        } else {
            throw new CustomException("from academycontroller delacademy");
        }
    }

    @GetMapping("/insertAcademy")
    public String insertAcademy(Model model){
        model.addAttribute("dto",new AcademyDTO());
        model.addAttribute("insertAction","/insertAcademy");
        model.addAttribute("top", "학원홍보");
        return "boardinsert";
    }

    @PostMapping("/insertAcademy")
    public String insertAcademyResult(@ModelAttribute AcademyDTO dto){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        dto.setEmail(auth.getName());
        Long newAid = academyService.insertAcademy(dto,saveFolder);
        return "redirect:/academy/"+newAid;
    }

    @GetMapping("/updateAcademy/{aid}")
    public String updateAcademy(@PathVariable Long aid, Model model){
        AcademyDTO dto = academyService.findByAid(aid);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth.getName().equals(dto.getEmail())
                || auth.getAuthorities().toString().equals("[ROLE_ADMIN]")){
            if(dto.getImgPath()!=null) {
                String[] imgPaths = dto.getImgPath().split(";-;");
                for(int i=0; i<imgPaths.length; i++){
                    imgPaths[i] = "/getAcaImages/"+imgPaths[i];
                }
                model.addAttribute("imgPaths",imgPaths);
            }
            model.addAttribute("dto",dto);
            System.out.println(dto.getTitle());
            model.addAttribute("insertAction", "/updateAcademy/"+aid);
            model.addAttribute("top", "학원홍보");
            return "boardupdate";
        } else {
            throw new CustomException("from academycontroller updateacademy");
        }

    }

    @PostMapping("/updateAcademy/{aid}")
    public String updateNoticeResult(@PathVariable Long aid, @ModelAttribute("dto") AcademyDTO dto){
        log.info("AcademyController getDelImgPaths( delete target )...{}", dto.getImgPaths());
        dto.setAid(aid);
        Long modAid = academyService.updateAcademy(dto, saveFolder);
        return "redirect:/academy/" + modAid;
    }
}
