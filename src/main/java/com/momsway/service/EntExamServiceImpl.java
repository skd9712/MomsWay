package com.momsway.service;

import com.momsway.domain.EntExam;
import com.momsway.dto.EntExamDTO;
import com.momsway.repository.entexam.EntExamRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EntExamServiceImpl implements EntExamService{
    private final EntExamRepository entExamRepository;
    private final ModelMapper modelMapper;
//    @Override
//    public List<EntExamDTO> entlist() {
//
//        List<EntExam> entList = entExamRepository.orderlist();
//        List<EntExamDTO> entDTOList = entList.stream().map(item ->
//                        modelMapper.map(item, EntExamDTO.class))
//                .collect(Collectors.toList());
//        return entDTOList;
//    }

    @Override
    public Page<EntExamDTO> entlist(Pageable pageable) {
        Page<EntExamDTO> orderlist = entExamRepository.orderlist(pageable);
        return orderlist;
    }
}
