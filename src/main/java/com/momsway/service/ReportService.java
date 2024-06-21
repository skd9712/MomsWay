package com.momsway.service;

import com.momsway.domain.Report;
import com.momsway.dto.ReportDTO;

import java.util.List;

public interface ReportService {
    int delReport(ReportDTO dto);

   List<ReportDTO> findAllReport();





}
