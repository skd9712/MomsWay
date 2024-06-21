package com.momsway.repository.report;

import com.momsway.domain.Report;

import java.util.List;

public interface ReportQueryDSL {
    List<Report> findByEid(Long eid);


    List<Report> findAllReport();
}
