package com.momsway.controller;

import com.momsway.dto.ReportDTO;
import com.momsway.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/report")
    public String report(@PageableDefault(size = 10, page = 0)Pageable pageable, Model model) {
        Page<ReportDTO> reportlist=reportService.findAllReport(pageable);
        int pagesize=5;
        int startPage=((int) (Math.ceil(pageable.getPageNumber() /pagesize))) * pagesize +1;
        int endPage=Math.min(startPage + pagesize -1, reportlist.getTotalPages());

//        Map<Long, Long> countReportsByEid = reportService.countReportsByEid();

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("reportlist", reportlist);
//        model.addAttribute("countReportsByEid", countReportsByEid);
        // 신고 리스트 불러오는 로직
        return "report/reportlist";
    }


    /** 신고삭제처리 */
    @GetMapping("/delreport/{rid}")
    public ResponseEntity<String> delReport(@PathVariable Long rid) {
        int result = reportService.delReports(rid);
        String msg = result == 1 ? "처리완료" : "처리오류";
        return ResponseEntity.ok().body(msg);
    }
    @PostMapping("/entreport")
    public ResponseEntity<String> EntReport(@RequestParam Long eid, @RequestParam Long uid, @RequestParam String comment) {
        try {
            ReportDTO reportDTO = new ReportDTO();
            reportDTO.setEid(eid);
            reportDTO.setUid(uid);
            reportDTO.setComment(comment);
            int result = reportService.EntReport(reportDTO);
            String msg;
            if (result == 1) {
                msg = "신고 성공";
            } else {
                msg = "신고 실패";
            }
            return ResponseEntity.ok().body(msg);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("신고 처리 중 오류가 발생했습니다.");
        }
    }

    @GetMapping("/repdetail/{rid}")
    public String reportDetail(@PathVariable Long rid, Model model){
        ReportDTO dto=reportService.detail(rid);
//        Map<Long, Long> countReportsByEid = reportService.countReportsByEid();
//        Long count = countReportsByEid.get(dto.getEid());
        model.addAttribute("dto", dto);
//        model.addAttribute("count", count);
        return "report/reportdetail";
    }



}
