package com.momsway.controller;

import com.momsway.dto.ReportDTO;
import com.momsway.service.ReportService;
import lombok.RequiredArgsConstructor;
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
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/report")
    public String report(@PageableDefault(size = 10, page = 0)Pageable pageable, Model model) {
        Page<ReportDTO> reportlist=reportService.findAllReport(pageable);
        int pagesize=5;
        int startPage=((int) (Math.ceil(pageable.getPageNumber() /pagesize))) * pagesize +1;
        int endPage=Math.min(startPage + pagesize -1, reportlist.getTotalPages());
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("reportlist", reportlist);
        // 신고 리스트 불러오는 로직
        return "report/reportlist";
    }

    /** 신고삭제처리 */
//    @GetMapping("/delreport/{rid}")
//    public ResponseEntity<String> delReport(@PathVariable Long rid){
//        int result=reportService.delReports(rid);
//        String msg="메롱";
//        if (result==0){
//            msg="처리오류";
//        }else {
//            msg="처리완료";
//        }
//        return ResponseEntity.ok().body(msg);
//    }
    @GetMapping("/delreport/{eid}")
    public ResponseEntity<String> delReport(@PathVariable Long eid) {
        int result = reportService.delReports(eid);
        String msg = result == 1 ? "처리완료" : "처리오류";
        return ResponseEntity.ok().body(msg);
    }
    @PostMapping("/entreport")
    public ResponseEntity<String> createReport(@RequestParam Long eid, @RequestParam Long uid, @RequestParam String comment) {
        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setEid(eid);
        reportDTO.setUid(uid);
        reportDTO.setComment(comment);
        int result = reportService.EntReport(reportDTO);
        String msg = result == 1 ? "신고 성공" : "신고 실패";
        return ResponseEntity.ok().body(msg);
    }





}
