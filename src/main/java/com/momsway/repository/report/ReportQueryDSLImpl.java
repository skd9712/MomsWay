package com.momsway.repository.report;

import static com.momsway.domain.QReport.*;

import com.momsway.domain.Report;
import com.momsway.dto.ReportDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
                .innerJoin(report.reportEntExam, entExam)
                .fetchJoin()
                .where(report.reportEntExam.eid.eq(eid))
                .fetch();
        return statuses;
    }

    @Override
    public Page<Report> findAllReport(Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(report.status.eq(false)); // status가 false인 데이터만 선택

        List<Report> fetch = queryFactory.select(report)
                .from(report)
                .join(report.reportUser, user)
                .fetchJoin()
                .join(report.reportEntExam, entExam)
                .fetchJoin()
                .where(builder)
                .orderBy(report.rid.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = queryFactory.select(report.count())
                .from(report)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(fetch, pageable, totalCount);
    }


    @Override
    public List<Report> findByRid(Long rid) {
        List<Report> fetch = queryFactory.select(report)
                .from(report)
                .innerJoin(report.reportEntExam, entExam)
                .fetchJoin()
                .innerJoin(report.reportUser, user)
                .fetchJoin()
                .where(report.rid.eq(rid))
                .fetch();
        return fetch;
    }


}
