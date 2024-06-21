package com.momsway.service;

import com.momsway.domain.Notice;
import com.momsway.dto.NoticeDTO;
import com.momsway.repository.notice.NoticeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

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
    @Transactional
    public Page<NoticeDTO> findList(Pageable pageable) {
        long count = listCount();
        List<NoticeDTO> list = noticeRepository.findList(pageable);
        return new PageImpl<>(list,pageable,count);
    }

    @Override
    public long listCount() {
        return noticeRepository.count();
    }

    @Override
    public NoticeDTO findByNid(Long nid) {
        Notice detail = noticeRepository.findByNid(nid);
        List<String> imgPaths = detail.getNoticeImgs().stream()
                .map(img->img.getImgPath()).collect(Collectors.toList());
        return NoticeDTO.builder()
                .nid(detail.getNid())
                .notify(detail.getNotify())
                .title(detail.getTitle())
                .content(detail.getContent())
                .readNo(detail.getReadNo())
                .category(detail.getCategory())
                .createAt(detail.getCreateAt())
                .imgPaths(imgPaths)
                .build();
    }

    @Override
    public int delNotice(Long nid) {
        int result = 0;
        try{
            noticeRepository.deleteById(nid);
            result = 1;
        }catch (Exception e){

        }
        return result;
    }
}
