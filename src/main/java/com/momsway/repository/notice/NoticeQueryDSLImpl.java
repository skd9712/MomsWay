package com.momsway.repository.notice;

import com.momsway.domain.Notice;
import static com.momsway.domain.QNotice.*;

import static com.momsway.domain.QNoticeImg.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NoticeQueryDSLImpl implements NoticeQueryDSL {
    private final JPAQueryFactory queryFactory;
    @Override
    public Notice findByNid(Long nid) {
        Notice notice1 = queryFactory.select(notice)
                .from(notice)
                .leftJoin(notice.noticeImgs)
                .fetchJoin()
                .where(notice.nid.eq(nid))
                .fetchOne();
        return notice1;
    }
}
