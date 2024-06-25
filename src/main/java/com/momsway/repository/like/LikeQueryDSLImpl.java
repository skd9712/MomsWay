package com.momsway.repository.like;

import static com.momsway.domain.QEntLike.*;

import com.momsway.dto.LikeDTO;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
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

    @Override
    public boolean findByUidAndEid(Long uid, Long eid) {
        try {

            Long count = queryFactory.select(entLike.count())
                    .from(entLike)
                    .where(entLike.likeEntExam.eid.eq(eid), entLike.likeUser.uid.eq(uid))
                    .fetchOne();
            log.info("find uid: {}",uid);
            log.info("find eid: {}",eid);
            log.info("findByUidAndEid count: {}", count);
            return count>0;
        }catch (DataAccessException e){
            log.error("Error in findByUidAndEid",e);
            throw new RuntimeException("Error Runtime Like");
        }
    }
}
