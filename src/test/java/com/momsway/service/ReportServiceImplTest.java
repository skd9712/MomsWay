package com.momsway.service;


import com.momsway.dto.ReportDTO;
import org.junit.jupiter.api.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ReportServiceImplTest {

    @Autowired
    private ReportService reportService;

    @Test
    void t1(){
        assertThat(reportService).isNotNull();
    }
//    @Test
//    void findAllReport() {
//        Page<ReportDTO> allReport = reportService.findAllReport();
//        Logger logger = LoggerFactory.getLogger(ReportServiceImplTest.class);
//
//         for(ReportDTO item:allReport){
//             logger.info("...reportList test...{}", item.getRid() );
//             logger.info("...reportList test...{}", item.getEid() );
//             logger.info("...reportList test...{}", item.getUid() );
//
//         }
//
//
//    }

    @Test
    void delReport(){

    }


//    @Test
//    void t2(){
//        List<Long> list = reportService.countReportsByEid();
//        for(Long l:list)
//            System.out.println(l);
//    }
//



}