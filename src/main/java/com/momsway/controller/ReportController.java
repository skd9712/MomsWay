package com.momsway.controller;

import com.momsway.dto.ReportDTO;
import com.momsway.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;
    @GetMapping("/report")
    public String report(){
        // 신고 리스트 불러오는 로직
        return "reportlist";
    }

    @PostMapping("/delreport")
    public @ResponseBody ResponseEntity<Integer> delReport(@RequestBody ReportDTO dto){
        // 해당 신고글 삭제 및 부대작업 와르르
        int result = reportService.delReport(dto);
        return ResponseEntity.ok().body(result);
    }
}
