package com.momsway.repository.entexam;

import com.momsway.domain.EntExam;
import static com.momsway.domain.QEntExam.entExam;
import static com.momsway.domain.QEntLike.entLike;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class EntExamQueryDSLImpl implements EntExamQueryDSL {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Tuple> entExamLikeSortList() {

        List<Tuple> entLikeSortList
                = queryFactory.select(entExam, entLike.count())
                .from(entExam)
                .innerJoin(entExam.entExamEntLikes, entLike)
                .fetchJoin()
                .groupBy(entLike.likeEntExam.eid)
                .orderBy(entLike.count().desc())
                .offset(0)
                .limit(5)
                .fetch();

        return entLikeSortList;
    }
}
