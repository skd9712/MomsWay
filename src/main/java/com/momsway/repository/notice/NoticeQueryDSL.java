package com.momsway.repository.notice;

import com.momsway.domain.Notice;

import java.util.List;

public interface NoticeQueryDSL {
    Notice findByNid(Long nid);

    List<Notice> noticeLatest();
}
