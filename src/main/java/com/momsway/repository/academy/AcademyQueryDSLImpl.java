package com.momsway.repository.academy;

import com.momsway.domain.Academy;
import static com.momsway.domain.QAcademy.academy;

import static com.momsway.domain.QUser.*;
import com.momsway.dto.AcademyDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AcademyQueryDSLImpl implements AcademyQueryDSL{

    private final JPAQueryFactory queryFactory;

    @Override
    public AcademyDTO findByAid(Long aid) {
        AcademyDTO academyDTO = queryFactory.select(Projections.fields(AcademyDTO.class
                        , academy.aid
                        , academy.title
                        , academy.content
                        , academy.readNo
                        , academy.createAt
                        , academy.imgPath
                        , user.email
                        , user.nickname))
                .from(academy)
                .leftJoin(academy.academyUser, user)
                .where(academy.aid.eq(aid))
                .fetchOne();
        return academyDTO;
    }

    @Override
    public List<Academy> academyList() {
        List<Academy> academyList
                = queryFactory.select(academy)
                .from(academy)
                .orderBy(academy.createAt.desc())
                .offset(0)
                .limit(5)
                .fetch();
        return academyList;
    }

}
