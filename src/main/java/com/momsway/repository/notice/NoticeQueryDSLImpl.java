package com.momsway.repository.notice;

import com.momsway.domain.Notice;
import static com.momsway.domain.QNotice.*;

import static com.momsway.domain.QNoticeImg.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    @Override
    public List<Notice> noticeLatest() {
        List<Notice> noticeList
                = queryFactory.select(notice)
                .from(notice)
                .orderBy(notice.createAt.desc())
                .offset(0)
                .limit(5)
                .fetch();
        return noticeList;
    }
}
