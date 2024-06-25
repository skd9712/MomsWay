package com.momsway.repository.report;

import static com.momsway.domain.QReport.*;

import com.momsway.domain.Report;
import com.momsway.dto.ReportDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
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
import java.util.stream.Collectors;

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
                .groupBy(report.reportEntExam.eid)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = queryFactory.select(report.count())
                .from(report)
                .where(builder)
                .fetchOne();

//        List<Tuple> eidCounts = queryFactory.select(report.reportEntExam.eid, report.count())
//                .from(report)
//                .where(builder)
//                .groupBy(report.reportEntExam.eid)
//                .fetch();


        return new PageImpl<>(fetch, pageable, totalCount);
    }

    @Override
    public List<Tuple> findEidCounts() {
        return null;
    }

    @Override
    public List<ReportDTO> findByRid(Long rid) {
        List<Tuple> tuples = queryFactory
                .select(report.rid, report.comment, entExam.eid, user.uid, entExam.eid.count())
                .from(report)
                .innerJoin(report.reportUser, user)
                .innerJoin(report.reportEntExam, entExam)
                .where(report.rid.eq(rid))
                .groupBy(entExam.eid)
                .orderBy(entExam.eid.count().desc())
                .fetch();

        List<ReportDTO> list = tuples.stream().map(tuple -> {
            ReportDTO dto = new ReportDTO();
            dto.setRid(tuple.get(report.rid));
            dto.setComment(tuple.get(report.comment));
            dto.setEid(tuple.get(entExam.eid));
            dto.setUid(tuple.get(user.uid));
//            dto.setCount(tuple.get(entExam.eid.count()));
            return dto;
        }).collect(Collectors.toList());

        return list;
    }


//    @Override
//    public List<Long> countByEid() {
//        queryFactory.select()
//                .from()
//                .groupBy();
//        return null;
//    }


}
