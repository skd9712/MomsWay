package com.momsway.controller;

import com.momsway.dto.ReportDTO;
import com.momsway.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;
    /** 신고리스트 불러오기 */


    @GetMapping("/report")
    public String report(Model model) {
        List<ReportDTO> reportlist = reportService.findAllReport();
        model.addAttribute("reportlist", reportlist);
        // 신고 리스트 불러오는 로직
        return "report/reportlist";
    }

//    @GetMapping("/report/{rid}")
//    public String detail(@PathVariable Long rid, Model model){
//        ReportDTO dto=
//    }

    @PostMapping("/delreport")
    public @ResponseBody ResponseEntity<Integer> delReport(@RequestBody ReportDTO dto){
        // 해당 신고글 삭제 및 부대작업 와르르
        int result = reportService.delReport(dto);
        return ResponseEntity.ok().body(result);
    }


}
