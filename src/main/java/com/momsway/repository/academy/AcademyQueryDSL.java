package com.momsway.repository.academy;

import com.momsway.domain.Academy;
import com.momsway.dto.AcademyDTO;

import java.util.List;

public interface AcademyQueryDSL {
    List<Academy> academyList();

    AcademyDTO findByAid(Long aid);
}
