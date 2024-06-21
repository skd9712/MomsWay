package com.momsway.repository.entexam;

import com.momsway.domain.EntExam;
import com.momsway.dto.EntExamDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EntExamQueryDSL {
    Page<EntExamDTO> orderlist(Pageable pageable);


//    List<EntExam> orderlist();
}
