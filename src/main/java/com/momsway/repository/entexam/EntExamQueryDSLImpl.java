package com.momsway.repository.entexam;

import com.momsway.domain.EntExam;
import com.momsway.domain.QEntExam;
import com.momsway.dto.EntExamDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import static com.momsway.domain.QEntExam.*;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class EntExamQueryDSLImpl implements EntExamQueryDSL {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<EntExamDTO> orderlist(Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        List<EntExamDTO> list = queryFactory.select(Projections.fields(EntExamDTO.class, entExam.eid, entExam.title, entExam.createAt, entExam.readNo))
                .from(entExam)
                .orderBy(entExam.eid.desc())
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long totalCount = queryFactory.select(entExam.count())
                .from(entExam)
                .where(builder)
                .fetchOne();
        return new PageImpl<>(list,pageable,totalCount);
    }


//    @Override
//    public List<EntExam> orderlist() {
//        List<Tuple> fetch
//                = queryFactory.select(entExam.eid, entExam.title, entExam.createAt, entExam.readNo).from(entExam).orderBy(entExam.eid.desc()).fetch();
//
//        return fetch.stream().map(tuple -> {
//            EntExam entExam = new EntExam();
//            entExam.setEid(tuple.get(QEntExam.entExam.eid));
//            entExam.setTitle(tuple.get(QEntExam.entExam.title));
//            entExam.setCreateAt(tuple.get(QEntExam.entExam.createAt));
//            entExam.setReadNo(tuple.get(QEntExam.entExam.readNo));
//            return entExam;
//        }).collect(Collectors.toList());
//    }
}
