package com.momsway.repository.entexam;

import com.momsway.domain.EntExam;
import com.querydsl.core.Tuple;

import java.util.List;

public interface EntExamQueryDSL {
    List<Tuple> entExamLikeSortList();
}
