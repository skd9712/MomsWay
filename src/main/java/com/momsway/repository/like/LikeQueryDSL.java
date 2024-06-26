package com.momsway.repository.like;

import com.momsway.dto.EntExamDTO;

import java.util.List;
import java.util.Optional;

public interface LikeQueryDSL {
    List<EntExamDTO> findByUid(long uidByEmail);

    Long findByUidAndEid(Long uid, Long eid,String username);
}
