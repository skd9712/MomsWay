package com.momsway.repository.user;

import com.momsway.domain.User;
import com.momsway.dto.AcademyDTO;
import com.momsway.dto.EntExamDTO;

import org.springframework.data.domain.Pageable;

import com.momsway.dto.UserDTO;


import java.util.List;

public interface UserQueryDSL {
    List<EntExamDTO> myentlist(long uidByEmail);

    int getCount(String search, String search_txt);

    UserDTO findByUserInfo(long uidByEmail);

    List<AcademyDTO> myacalist(long uidByEmail);
}
