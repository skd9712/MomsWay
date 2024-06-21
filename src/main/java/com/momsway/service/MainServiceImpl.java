package com.momsway.service;

import com.momsway.domain.EntExam;
import com.momsway.dto.EntExamDTO;
import com.momsway.repository.entexam.EntExamRepository;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MainServiceImpl implements MainService{

    private final EntExamRepository entExamRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<Tuple> entExamLikeSortList() {
        List<Tuple> entExamList
                = entExamRepository.entExamLikeSortList();

        return entExamList;
    }
}
