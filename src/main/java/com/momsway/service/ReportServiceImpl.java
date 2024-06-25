package com.momsway.service;

import com.momsway.domain.EntExam;
import com.momsway.domain.Report;
import com.momsway.domain.User;
import com.momsway.domain.UserRole;
import com.momsway.dto.ReportDTO;
import com.momsway.repository.entexam.EntExamRepository;
import com.momsway.repository.report.ReportRepository;
import com.momsway.repository.user.UserRepository;
import com.querydsl.core.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.momsway.domain.QReport.report;

@Service
@RequiredArgsConstructor

public class ReportServiceImpl implements ReportService {
    private static final Logger log = LoggerFactory.getLogger(ReportService.class);
    private final EntExamRepository entExamRepository;
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    @Override
    public Page<ReportDTO> findAllReport(Pageable pageable) {
        Page<Report> reportPage = reportRepository.findAllReport(pageable);
        List<ReportDTO> reportDTOList = reportPage.stream().map(item ->
                ReportDTO.builder()
                .rid(item.getRid())
                .uid(item.getReportUser().getUid())
                .eid(item.getReportEntExam().getEid())
                .status(item.getStatus())
                .comment(item.getComment())
                .build()).collect(Collectors.toList());

        List<Long> countByEid = reportRepository.countByEid();
        PageImpl<ReportDTO> reportDTOS = new PageImpl<>(reportDTOList, pageable, reportPage.getTotalElements());
        return new PageImpl<>(reportDTOList, pageable, reportPage.getTotalElements());
    }

    @Override
    public List<Long> countReportsByEid() {
        return reportRepository.countByEid();
    }

    @Override
    public ReportDTO detail(Long rid) {
        List<ReportDTO> byRid = reportRepository.findByRid(rid);
        if (byRid.isEmpty()) {
            return null; // 또는 예외를 던질 수도 있습니다.
        }

        return byRid.get(0);
    }



    @Override
    @Transactional
    public int delReports(Long rid) {
        try {
            // Report를 찾습니다.
            Optional<Report> reportOpt = reportRepository.findById(rid);
            if (reportOpt.isPresent()) {
                Report report = reportOpt.get();

                // 해당 사용자의 삭제 횟수 증가
                User user = report.getReportUser();
                user.setReportNo(user.getReportNo() + 1);

                // report_no가 3번 이상일 때 userrole 변경
                if (user.getReportNo() >= 3) {
                    user.setRole(UserRole.SUSPENDED); // 변경할 역할로 설정
                }

                userRepository.save(user);

                // 해당 eid를 가진 모든 Report의 status를 true로 설정
                List<Report> eidReports = reportRepository.findByEid(report.getReportEntExam().getEid());
                for (Report r : eidReports) {
                    r.setStatus(true);
                    reportRepository.save(r); // 각 Report를 저장
                }
                EntExam entExam=report.getReportEntExam();
                entExamRepository.deleteById(entExam.getEid());


                return 1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }


    @Override
    public int EntReport(ReportDTO reportDTO) {
        try {
            User user=userRepository.findById(reportDTO.getUid()).orElseThrow(()-> new RuntimeException("User not found"));
            EntExam entExam=entExamRepository.findById(reportDTO.getEid()).orElseThrow(()-> new RuntimeException("EntExam not found"));

            Report report=Report.builder()
                    .comment(reportDTO.getComment())
                    .status(false) //초기 신고 상태는 false로 설정
                    .reportUser(user)
                    .reportEntExam(entExam)
                    .build();

            reportRepository.save(report);
            return 1;
        }catch (Exception e){
            System.out.println(e);
            return 0;
        }

    }


}
