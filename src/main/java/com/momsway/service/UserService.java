package com.momsway.service;

import com.momsway.dto.EntExamDTO;
import com.momsway.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    Long joinUser(UserDTO dto);
    boolean findEmailCheck(String email);
    boolean findNicknameCheck(String nickname);


    long findUidByEmail(String sessionId);

    List<EntExamDTO> myentlist(String username);

    List<EntExamDTO> findByUid(String username);

    Page<UserDTO> findUsers(Pageable pageable, String search, String search_txt);

    UserDTO getUserDetail(Long uid);

    Long deleteUser(Long uid);
}
