package com.momsway.service;

import com.momsway.dto.UserDTO;

public interface UserService {
    Long joinUser(UserDTO dto);
    boolean findEmailCheck(String email);
    boolean findNicknameCheck(String nickname);
}
