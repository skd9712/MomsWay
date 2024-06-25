package com.momsway.repository.entexam;

import com.momsway.domain.EntExam;

import static com.momsway.domain.QEntExam.entExam;
import static com.momsway.domain.QEntLike.entLike;
import static com.momsway.domain.QEntReply.entReply;

import com.momsway.domain.QEntExam;
import com.momsway.dto.EntExamDTO;
import com.momsway.dto.EntReplyDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Repository;
import org.thymeleaf.spring6.expression.Fields;

import static com.momsway.domain.QEntExam.*;
import static com.momsway.domain.QUser.*;

import java.util.List;
import java.util.stream.Collectors;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class EntExamQueryDSLImpl implements EntExamQueryDSL {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Tuple> entExamLikeSortList() {

        List<Tuple> list = queryFactory.select(entExam.eid, entExam.title, entLike.likeEntExam.eid.count())
                .from(entExam)
                .leftJoin(entExam.entExamEntLikes, entLike)
                .groupBy(entExam.eid)
                .orderBy(entLike.likeEntExam.eid.count().desc())
                .offset(0)
                .limit(5)
                .fetch();

        return list;
    }
  
    @Override
    public Page<EntExamDTO> orderlist(Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        List<EntExamDTO> list = queryFactory.select(Projections.fields(EntExamDTO.class, entExam.eid, entExam.title,user.nickname, entExam.createAt, entExam.readNo))
                .from(entExam)
                .innerJoin(entExam.entExamUser, user)
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

    @Override
    public EntExamDTO findByEid(Long eid) {
        EntExamDTO entExamDTO = queryFactory.select(Projections.fields(EntExamDTO.class, entExam.eid, entExam.title, entExam.content, entExam.createAt, entExam.readNo, user.nickname,entExam.imgPath))
                .from(entExam)
                .innerJoin(entExam.entExamUser, user)
                .where(entExam.eid.eq(eid))
                .fetchOne();
        return entExamDTO;

    }

    @Override
    public List<EntExam> entExamLatestList() {
        List<EntExam> entExamList
                = queryFactory.select(entExam)
                .from(entExam)
                .orderBy(entExam.createAt.desc())
                .offset(0)
                .limit(5)
                .fetch();
        return entExamList;
    }

    @Override
    public List<EntReplyDTO> repList(Long eid) {
        List<EntReplyDTO> repList = queryFactory.select(Projections.fields(EntReplyDTO.class, entReply.repId,entReply.createAt,entReply.content,entExam.eid,user.uid, user.nickname))
                .from(entReply)
                .innerJoin(entReply.replyEntExam, entExam)
                .innerJoin(entReply.entReplyUser, user)
                .where(entExam.eid.eq(eid))
                .fetch();
        return repList;
    }


}
