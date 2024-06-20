package com.momsway.repository.notice;

import com.momsway.domain.Academy;
import com.momsway.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice,Long>, NoticeQueryDSL {
}
