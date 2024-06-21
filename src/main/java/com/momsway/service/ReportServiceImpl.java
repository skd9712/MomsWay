package com.momsway.service;

import com.momsway.domain.Report;
import com.momsway.domain.User;
import com.momsway.dto.ReportDTO;
import com.momsway.repository.entexam.EntExamRepository;
import com.momsway.repository.report.ReportRepository;
import com.momsway.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final EntExamRepository entExamRepository;
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    private final ModelMapper modelMapper;
    @Override
    @Transactional
    public int delReport(ReportDTO dto) {
        /* 1) 글 테이블에서 해당글 삭제 (JPA 내장 메서드)
           2) 신고 테이블에서 처리여부 변경 (JPA 내장메서드로 find 후 setter 사용)
           3) 유저 테이블에서 글쓴이의 삭제횟수 +1
         */
        int result = 0;
        try{
            entExamRepository.deleteById(dto.getEid());
            List<Report> eid = reportRepository.findByEid(dto.getEid());
            for(Report r:eid){
                r.setStatus(true);
            }
            Optional<User> uid = userRepository.findById(dto.getUid());
            User user = uid.orElseThrow(()->{throw new RuntimeException();});
            user.setReportNo(user.getReportNo()+1);
            result = 1;
        }catch (Exception e){

        }
        return result;
    }


    @Override
    public List<ReportDTO> findAllReport() {
        List<Report> reportlist = reportRepository.findAllReport();
        List<ReportDTO> collect = reportlist.stream().map(item -> {

            ReportDTO build = ReportDTO.builder()
                    .rid(item.getRid())
                    .uid(item.getReportUser().getUid())
                    .eid(item.getReportEntExam().getEid())
                    .status(item.getStatus())
                    .build();
            return build;
        }).collect(Collectors.toList());


        return collect;
    }

//    @Override
//    public List<ReportDTO> reportlist() {
//       List<Report> reporboardlist=reportRepository.findAllReport();
//       List<ReportDTO> reportDTOList=reporboardlist.stream()
//               .map(item -> modelMapper.map(item,ReportDTO.class))
//               .collect(Collectors.toList());
//        return reportDTOList;
//    }

}
