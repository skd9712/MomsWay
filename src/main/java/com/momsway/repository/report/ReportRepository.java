package com.momsway.repository.report;

import com.momsway.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ReportRepository extends JpaRepository<Report,Long>, ReportQueryDSL {

    List<Report> findByEid(Long eid);

    @Query("select count (r.rid) from Report r " +
            " group by r.reportEntExam.eid " +
            " order by r.rid desc ")
    List<Long> countByEid();



}
