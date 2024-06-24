package com.momsway.service;

import com.momsway.domain.Notice;
import com.momsway.domain.NoticeImg;
import com.momsway.dto.NoticeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NoticeService {
    Page<NoticeDTO> findList(Pageable pageable);
    long listCount();

    List<NoticeDTO> findTopList();

    NoticeDTO findByNid(Long nid);

    int delNotice(Long nid);

    Long insertNotice(NoticeDTO dto, String saveFolder);

}
