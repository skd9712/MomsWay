package com.momsway.service;

import com.momsway.dto.LikeDTO;
import com.momsway.repository.like.LikeRepository;

import java.util.List;

public interface LikeService {

    void insertLike(LikeDTO dto);

    int delLike(Long lid);

    List<String> findByUid(Long uid);
}
