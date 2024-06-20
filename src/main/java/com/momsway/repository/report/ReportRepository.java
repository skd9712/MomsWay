package com.momsway.repository.report;

import com.momsway.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface ReportRepository extends JpaRepository<Report,Long>, ReportQueryDSL {
}
