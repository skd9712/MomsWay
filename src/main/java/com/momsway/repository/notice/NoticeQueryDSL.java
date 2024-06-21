package com.momsway.repository.notice;

import com.momsway.domain.Notice;

public interface NoticeQueryDSL {
    Notice findByNid(Long nid);
}
