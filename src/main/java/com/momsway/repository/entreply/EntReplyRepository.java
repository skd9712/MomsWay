package com.momsway.repository.entreply;

import com.momsway.domain.Academy;
import com.momsway.domain.EntReply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntReplyRepository extends JpaRepository<EntReply,Long>, EntReplyQueryDSL {
}
