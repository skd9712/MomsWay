package com.momsway.service;

import com.momsway.dto.EntExamDTO;
import com.momsway.dto.UserDTO;

import java.util.List;

public interface UserService {
    Long joinUser(UserDTO dto);
    boolean findEmailCheck(String email);
    boolean findNicknameCheck(String nickname);


    long findUidByEmail(String sessionId);

    List<EntExamDTO> myentlist(String username);

    List<EntExamDTO> findByUid(String username);
}
