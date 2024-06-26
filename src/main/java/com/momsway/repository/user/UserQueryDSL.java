package com.momsway.repository.user;

import com.momsway.dto.EntExamDTO;

import java.util.List;

public interface UserQueryDSL {
    List<EntExamDTO> myentlist(long uidByEmail);
}
