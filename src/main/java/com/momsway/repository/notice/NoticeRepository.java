package com.momsway.repository.notice;

import com.momsway.domain.Academy;
import com.momsway.domain.Notice;
import com.momsway.dto.NoticeDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice,Long>, NoticeQueryDSL {
    @Query(" select new com.momsway.dto.NoticeDTO(n.nid, n.notify, n.category, n.title, n.readNo, n.createAt) " +
            "from Notice n order by n.nid desc")
    List<NoticeDTO> findList(Pageable pageable);

    @Override
    long count();

    @Query(" select n.nid, n.title, n.createAt, n.category from Notice n where n.notify = true")
    List<Object[]> findTopList();
}
