package com.momsway.repository.report;

import com.momsway.domain.EntExam;
import com.momsway.domain.Report;
import com.momsway.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ReportRepository extends JpaRepository<Report,Long>, ReportQueryDSL {

    List<Report> findByEid(Long eid);

//    @Query("select count (r.rid) from Report r " +
//            " group by r.reportEntExam.eid " +
//            " order by r.rid desc ")
//    List<Long> countByEid();

    @Query("select r.reportEntExam.eid, count(r.rid) from Report r " +
            "group by r.reportEntExam.eid " +
            "order by r.rid desc")
    List<Object[]> countByEid();

    @Query( " select  count(r) from Report  r where r.reportEntExam.eid=:eid")
    long countByReportEid(Long eid);


    //boolean existsByReportUserAndReportEntExam(User user, EntExam entExam);


}
