package com.momsway.repository.noticeimg;

import com.momsway.domain.Academy;
import com.momsway.domain.NoticeImg;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeImgRepository extends JpaRepository<NoticeImg,Long>, NoticeImgQueryDSL {
}
