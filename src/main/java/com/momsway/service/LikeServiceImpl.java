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
public class LikeServiceImpl implements LikeService {
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
    public boolean findLike(Long uid, Long eid) {
        log.info("Sereid...{}",eid);
        log.info("Seruid...{}",uid);

        return likeRepository.findByUidAndEid(uid,eid);
    }

    @Override
    public void insertLike(LikeDTO dto) {
        log.info("eid...{}", dto.getEid());
        Optional<EntExam> entExam = entExamRepository.findById(dto.getEid());
//        Optional<User> user = userRepository.findById(dto.getUid());
//        User parent2 = user.orElseThrow(() -> new RuntimeException("error User"));
        if (entExam.isPresent()) {
            EntExam parent = entExam.orElseThrow(() -> new RuntimeException("error EntExam"));
            log.info("Found EntExam with id: {}", parent.getEid());
            User defaultUser = userRepository.findById(1L).orElseThrow(() -> new RuntimeException("Default User not found"));
            log.info("Found default user with id: {}", defaultUser.getUid());
            boolean alreadyLiked = likeRepository.findByUidAndEid(defaultUser.getUid(),parent.getEid());
            log.info("..t/f..{}",alreadyLiked);
            if (alreadyLiked) {
                log.info("User with id: {} has already liked the EntExam with id: {}", defaultUser.getUid(),parent.getEid());
                return;
            }
            EntLike like = EntLike.builder()
                    .likeEntExam(parent)
                    .likeUser(defaultUser)
                    .build();
            likeRepository.save(like);
            log.info("Saved new like for user with id: {} and EntExam with id: {}", defaultUser.getUid(), parent.getEid());
        }else{
            log.error("EntExam with id: {} not found", dto.getEid());
            throw new RuntimeException("Error occurred while inserting like");
        }
    }

    @Override
    public int delLike(Long lid) {
        int result = 0;
        try {
            likeRepository.deleteById(lid);
            result = 1;
        } catch (Exception e) {
            log.error("LikeService delLike....{}", e);
        }
        return result;
    }
}
