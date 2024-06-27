package com.momsway.service;

import com.momsway.dto.AcademyDTO;
import com.momsway.dto.NoticeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AcademyService {
    Page<AcademyDTO> findAcademyList(Pageable pageable, String search_txt);

    long listCount();

    AcademyDTO findByAid(Long aid);

    Long insertAcademy(AcademyDTO dto, String saveFolder);

    int delAcademy(Long aid, String saveFolder);

    Long updateAcademy(AcademyDTO dto, String saveFolder);

    void addAcademyReadNo(Long aid);
}
