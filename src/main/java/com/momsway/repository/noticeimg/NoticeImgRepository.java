package com.momsway.repository.noticeimg;

import com.momsway.domain.Academy;
import com.momsway.domain.NoticeImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeImgRepository extends JpaRepository<NoticeImg,Long>, NoticeImgQueryDSL {
    @Override
    <S extends NoticeImg> List<S> saveAll(Iterable<S> entities);
}
