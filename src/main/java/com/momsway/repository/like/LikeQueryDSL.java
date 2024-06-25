package com.momsway.repository.like;

import java.util.List;
import java.util.Optional;

public interface LikeQueryDSL {
    List<String> findByUid(Long uid);

    boolean findByUidAndEid(Long uid, Long eid);
}
