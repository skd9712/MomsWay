package com.momsway.service;

import com.momsway.dto.EntReplyDTO;

import java.util.List;

public interface EntReplyService {
    List<EntReplyDTO> repList(Long eid);

    Long insertRep(EntReplyDTO dto);
}
