package com.momsway.service;

import com.momsway.domain.EntExam;
import com.momsway.domain.EntLike;
import com.momsway.domain.User;
import com.momsway.dto.LikeDTO;
import com.momsway.repository.entexam.EntExamRepository;
import com.momsway.repository.like.LikeRepository;
import com.momsway.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeServiceImpl implements LikeService{
    private final LikeRepository likeRepository;
    private final EntExamRepository entExamRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<String> findByUid(Long uid) {
        List<String> likeTitles = likeRepository.findByUid(uid);
        return likeTitles;
    }

    @Override
    public void insertLike(LikeDTO dto) {
        Optional<EntExam> entExam = entExamRepository.findById(dto.getEid());
        EntExam parent = entExam.orElseThrow(()->new RuntimeException());
        Optional<User> user = userRepository.findById(dto.getUid());
        User parent2 = user.orElseThrow(()->new RuntimeException());
        EntLike like = EntLike.builder()
                        .likeEntExam(parent)
                        .likeUser(parent2)
                        .build();
        likeRepository.save(like);
    }

    @Override
    public int delLike(Long lid) {
        int result = 0;
        try{
            likeRepository.deleteById(lid);
            result =1;
        }catch (Exception e){
            log.error("LikeService delLike....{}",e);
        }
        return result;
    }
}
