package com.momsway.repository.report;

import static com.momsway.domain.QReport.*;

import com.momsway.domain.Report;
import com.momsway.dto.ReportDTO;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReportQueryDSLImpl implements ReportQueryDSL {
    private final JPAQueryFactory queryFactory;
    @Override
    public List<Report> findByEid(Long eid) {
        List<Report> statuses = queryFactory.select(report)
                .from(report)
                .innerJoin(report.reportEntExam)
                .fetchJoin()
                .where(report.reportEntExam.eid.eq(eid))
                .fetch();
        return statuses;
    }
}
