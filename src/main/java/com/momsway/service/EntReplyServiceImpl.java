package com.momsway.service;

import com.momsway.domain.EntExam;
import com.momsway.domain.EntReply;
import com.momsway.domain.User;
import com.momsway.dto.EntExamDTO;
import com.momsway.dto.EntReplyDTO;
import com.momsway.repository.entexam.EntExamRepository;
import com.momsway.repository.entreply.EntReplyRepository;
import com.momsway.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.momsway.domain.QEntExam.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class EntReplyServiceImpl implements EntReplyService{
    private final EntExamRepository entExamRepository;
    private final EntReplyRepository entReplyRepository;
    private final UserRepository userRepository;
    @Override
    public List<EntReplyDTO> repList(Long eid) {
        List<EntReplyDTO> repList = entExamRepository.repList(eid);
        return repList;
    }

    @Override
    public Long insertRep(EntReplyDTO dto,String username) {

        Optional<EntExam> byEid
                = entExamRepository.findById(dto.getEid());
        EntExam entExam1 = byEid.orElseThrow(() -> new IllegalArgumentException("invalid eid"));
        User user = userRepository.findByEmail(username);
        log.info("....eid...{}",dto.getEid());
        EntReply entity = EntReply.builder()
                .content(dto.getContent())
                .replyEntExam(entExam1)
                .entReplyUser(user)
                .build();
        EntReply save = entReplyRepository.save(entity);
        return save.getRepId();
    }
}