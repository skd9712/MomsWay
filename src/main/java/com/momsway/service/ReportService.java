package com.momsway.service;

import com.momsway.domain.Report;
import com.momsway.dto.ReportDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReportService {
    int delReport(ReportDTO dto);
    int delReports(Long rid);
    Page<ReportDTO> findAllReport(Pageable pageable);

}
