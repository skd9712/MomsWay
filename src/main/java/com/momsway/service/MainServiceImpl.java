package com.momsway.service;

import com.momsway.domain.Academy;
import com.momsway.domain.EntExam;
import com.momsway.domain.Notice;
import com.momsway.dto.AcademyDTO;
import com.momsway.dto.EntExamDTO;
import com.momsway.dto.LikeDTO;
import com.momsway.dto.NoticeDTO;
import com.momsway.repository.academy.AcademyRepository;
import com.momsway.repository.entexam.EntExamRepository;
import com.momsway.repository.notice.NoticeRepository;
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
    private final NoticeRepository noticeRepository;
    private final AcademyRepository academyRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<Object[]> entExamLikeSortList() {
        List<Tuple> entExamList
                = entExamRepository.entExamLikeSortList();

//        List<Object[]> list = entExamList.stream().map(item -> {
//            Object[] o = item.toArray();
//            return o;
//        }).collect(Collectors.toList());


        return null;
    }

    @Override
    public List<NoticeDTO> noticeLatestList() {
        List<Notice> noticeList
                = noticeRepository.noticeLatest();

        List<NoticeDTO> noticeLatestList
                = noticeList.stream()
                .map(item -> modelMapper.map(item, NoticeDTO.class))
                .collect(Collectors.toList());
        return noticeLatestList;
    }

    @Override
    public List<AcademyDTO> academyLatestList() {
        List<Academy> academyList
                = academyRepository.academyList();

        List<AcademyDTO> academyLatestList
                = academyList.stream()
                .map(item -> modelMapper.map(item, AcademyDTO.class))
                .collect(Collectors.toList());
        return academyLatestList;
    }

    @Override
    public List<EntExamDTO> entExamLatestList() {
        List<EntExam> entExamList
                = entExamRepository.entExamLatestList();

        List<EntExamDTO> entExamLatestList
                = entExamList.stream()
                .map(item -> modelMapper.map(item, EntExamDTO.class))
                .collect(Collectors.toList());
        return entExamLatestList;
    }
}
