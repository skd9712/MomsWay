package com.momsway.repository.academy;

import com.momsway.domain.Academy;
import static com.momsway.domain.QAcademy.academy;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AcademyQueryDSLImpl implements AcademyQueryDSL{

    private final JPAQueryFactory queryFactory;
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
