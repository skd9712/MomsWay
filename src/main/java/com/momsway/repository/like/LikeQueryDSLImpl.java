package com.momsway.repository.like;

import static com.momsway.domain.QEntLike.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class LikeQueryDSLImpl implements LikeQueryDSL {
    private final JPAQueryFactory queryFactory;
    @Override
    public List<String> findByUid(Long uid) {
        List<String> likeTitles = queryFactory.select(entLike.likeEntExam.title)
                .from(entLike)
                .innerJoin(entLike.likeEntExam)
                .fetchJoin()
                .where(entLike.likeUser.uid.eq(uid))
                .fetch();
        return likeTitles;
    }
}
