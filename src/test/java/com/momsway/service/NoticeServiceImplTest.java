//package com.momsway.service;
//
//import com.momsway.dto.NoticeDTO;
//import static org.assertj.core.api.Assertions.*;
//
//import com.momsway.repository.notice.NoticeRepository;
//import jakarta.transaction.Transactional;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//
//import java.util.List;
//
//@SpringBootTest
//class NoticeServiceImplTest {
//    @Autowired
//    private NoticeService noticeService;
//
//    @Autowired
//    private NoticeRepository noticeRepository;
//    @Test
//    public void findlist(Pageable pageable){
//        List<NoticeDTO> all = noticeRepository.findList(pageable);
//        assertThat(all).isNotNull();
//        for(NoticeDTO d:all){
//            System.out.println(d.getTitle());
//        }
//    }
//
//    @Test
//    public void findByNid(){
//        assertThat(noticeService.findByNid(1L).getImgPaths().get(0))
//                .isEqualTo("logo.png");
//    }
//
//}