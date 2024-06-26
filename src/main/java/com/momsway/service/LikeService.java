package com.momsway.service;

import com.momsway.dto.LikeDTO;
import com.momsway.repository.like.LikeRepository;

import java.util.List;

public interface LikeService {

    void insertLike(LikeDTO dto,String username);

    int delLike(Long lid);


    boolean findLike(Long uid, Long eid,String username);
}
