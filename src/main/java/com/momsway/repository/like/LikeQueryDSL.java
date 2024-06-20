package com.momsway.repository.like;

import java.util.List;

public interface LikeQueryDSL {
    List<String> findByUid(Long uid);
}
