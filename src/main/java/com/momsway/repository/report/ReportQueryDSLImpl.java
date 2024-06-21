package com.momsway.repository.report;

import static com.momsway.domain.QReport.*;

import com.momsway.domain.Report;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import static com.momsway.domain.QUser.user;
import static com.momsway.domain.QEntExam.entExam;
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

    @Override
    public List<Report> findAllReport() {
//        List<Long> reportlist = queryFactory.select(Projections.fields(
//                          report.rid
//                        , report.status
//                        , report.reportEntExam.eid
//                        , report.reportUser.uid
//                ))
//                .from(report)
//                .join(report.reportEntExam, entExam)
//                .join(report.reportUser, user)
//                .fetch();

        List<Report> fetch = queryFactory.select(report)
                .from(report)
                .join(report.reportUser, user )
                .fetchJoin()
                .join(report.reportEntExam, entExam)
                .fetchJoin()
                .fetch();

        return fetch;
    }


}
