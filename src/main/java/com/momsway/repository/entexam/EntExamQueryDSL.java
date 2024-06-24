package com.momsway.repository.entexam;

import com.momsway.domain.EntExam;

import com.momsway.dto.EntReplyDTO;
import com.querydsl.core.Tuple;
import com.momsway.dto.EntExamDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface EntExamQueryDSL {

    List<Tuple> entExamLikeSortList();

    Page<EntExamDTO> orderlist(Pageable pageable);

    EntExamDTO findByEid(Long eid);

    List<EntExam> entExamLatestList();

    List<EntReplyDTO> repList(Long eid);
}
