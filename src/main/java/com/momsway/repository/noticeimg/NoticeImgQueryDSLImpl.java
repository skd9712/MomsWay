package com.momsway.repository.noticeimg;

import com.momsway.domain.NoticeImg;
import static com.momsway.domain.QNoticeImg.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class NoticeImgQueryDSLImpl implements NoticeImgQueryDSL {

    private final JPAQueryFactory queryFactory;
    public List<NoticeImg> findAllByNid(Long nid){
        List<NoticeImg> noticeImgs = queryFactory.selectFrom(noticeImg)
                .innerJoin(noticeImg.notice)
                .where(noticeImg.notice.nid.eq(nid))
                .fetchJoin()
                .fetch();
        return noticeImgs;
    }

    @Override
    public void deleteAllByImgPath(String path) {
        queryFactory.delete(noticeImg)
                .where(noticeImg.imgPath.eq(path))
                .execute();
    }
}
