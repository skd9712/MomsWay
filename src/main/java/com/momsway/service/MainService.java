package com.momsway.service;

import com.momsway.dto.AcademyDTO;
import com.momsway.dto.EntExamDTO;
import com.momsway.dto.NoticeDTO;
import com.querydsl.core.Tuple;

import java.util.List;

public interface MainService {
    List<Object[]> entExamLikeSortList();

    List<NoticeDTO> noticeLatestList();

    List<AcademyDTO> academyLatestList();

    List<EntExamDTO> entExamLatestList();
}
