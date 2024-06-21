package com.momsway.service;

import com.momsway.domain.Notice;
import com.momsway.dto.NoticeDTO;
import com.momsway.repository.notice.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService{
    private final NoticeRepository noticeRepository;

    @Override
    public List<NoticeDTO> findTopList() {
        List<Object[]> toplist = noticeRepository.findTopList();
        List<NoticeDTO> result = toplist.stream().map(item -> NoticeDTO.builder()
                        .nid((Long) item[0]).title((String) item[1])
                        .createAt((LocalDateTime) item[2])
                        .category((String) item[3])
                        .build())
                .collect(Collectors.toList());
        return result;
    }

    @Override
    public Page<NoticeDTO> findList(Pageable pageable) {
        long count = listCount();
        List<NoticeDTO> list = noticeRepository.findList(pageable);
        return new PageImpl<>(list,pageable,count);
    }

    @Override
    public long listCount() {
        return noticeRepository.count();
    }
}
