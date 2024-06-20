package com.momsway.repository.report;

import com.momsway.domain.Report;
import com.momsway.dto.ReportDTO;

import java.util.List;

public interface ReportQueryDSL {
    List<Report> findByEid(Long eid);
}
