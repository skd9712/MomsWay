package com.momsway.repository.report;

import com.momsway.domain.Report;
import com.momsway.dto.ReportDTO;
import com.querydsl.core.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReportQueryDSL {

    List<Report> findByEid(Long eid);


    Page<Report> findAllReport(Pageable pageable);

    List<Tuple> findEidCounts();

    List<ReportDTO> findByRid(Long rid);

//    @Query("select r from Report r where r.reportEntExam.eid = :eid")
//    List<Report> findByEid(@Param("eid") Long eid);



//    List<Long> countByEid1();
}
