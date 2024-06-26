package com.momsway.repository.noticeimg;

import com.momsway.domain.NoticeImg;

import java.util.List;

public interface NoticeImgQueryDSL {
    List<NoticeImg> findAllByNid(Long nid);

    void deleteAllByImgPath(String path);
}
