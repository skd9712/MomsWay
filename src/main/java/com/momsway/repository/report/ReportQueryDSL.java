package com.momsway.repository.report;

import com.momsway.domain.Report;
import com.momsway.dto.ReportDTO;
import com.querydsl.core.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReportQueryDSL {
    List<Report> findByEid(Long eid);


    Page<Report> findAllReport(Pageable pageable);

    List<Tuple> findEidCounts();

    List<ReportDTO> findByRid(Long rid);


//    List<Long> countByEid();
}
