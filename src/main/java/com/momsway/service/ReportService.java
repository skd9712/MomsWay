package com.momsway.service;

import com.momsway.domain.Report;
import com.momsway.dto.ReportDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ReportService {

    Page<ReportDTO> findAllReport(Pageable pageable);

    int delReports(Long rid);


    int EntReport(ReportDTO reportDTO);

    List<Long> countReportsByEid();


    ReportDTO detail(Long rid);
}
