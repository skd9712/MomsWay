package com.momsway.repository.user;

import com.momsway.dto.EntExamDTO;
import com.momsway.dto.UserDTO;

import java.util.List;

public interface UserQueryDSL {
    List<EntExamDTO> myentlist(long uidByEmail);

    UserDTO findByUserInfo(long uidByEmail);
}
