package com.momsway.service;

import com.momsway.dto.LikeDTO;
import com.momsway.repository.like.LikeRepository;

import java.util.List;

public interface LikeService {

    Long insertLike(LikeDTO dto,String username);

    int delLike(Long lid);


    Long findLike(Long uid, Long eid,String username);
}
