package com.momsway.repository.like;

import static com.momsway.domain.QEntLike.*;
import static com.momsway.domain.QEntExam.*;

import com.momsway.dto.EntExamDTO;
import com.momsway.dto.LikeDTO;
import com.querydsl.core.types.Projections;
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
    public List<EntExamDTO> findByUid(long uidByEmail) {
        List<EntExamDTO> likeTitles = queryFactory.select(Projections.fields(EntExamDTO.class, entExam.title, entExam.eid))
                .from(entLike)
                .innerJoin(entLike.likeEntExam, entExam)
                .where(entLike.likeUser.uid.eq(uidByEmail))
                .fetch();
        return likeTitles;
    }

    @Override
    public Long findByUidAndEid(Long uid, Long eid,String username) {

        try {
            Long likeId = queryFactory.select(entLike.lid)
                    .from(entLike)
                    .where(entLike.likeEntExam.eid.eq(eid), entLike.likeUser.uid.eq(uid))
                    .fetchOne();
            log.info("find uid: {}",uid);
            log.info("find eid: {}",eid);
            log.info("findByUidAndEid likeId: {}", likeId);
            return likeId;
        }catch (DataAccessException e){
            log.error("Error in findByUidAndEid",e);
            throw new RuntimeException("Error Runtime Like");
        }
    }
}
