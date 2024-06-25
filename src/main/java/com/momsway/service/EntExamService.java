package com.momsway.service;

import com.momsway.dto.EntExamDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EntExamService {
    Page<EntExamDTO> entlist(Pageable pageable, String search_txt);

    long upload(String saveFolder, EntExamDTO dto);

    EntExamDTO findByEid(Long eid);

    int delEnt(Long eid);

    long entUpdate(Long eid, String saveFolder, EntExamDTO dto);
}
