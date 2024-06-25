package com.momsway.service;

import com.momsway.domain.User;
import com.momsway.domain.UserRole;
import com.momsway.dto.UserDTO;
import com.momsway.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final ModelMapper modelMapper;

    /** 회원가입 */
   @Override
    public Long joinUser(UserDTO dto) {
       // 비밀번호 암호화
       String pwd = encoder.encode(dto.getPwd());

       // Role
       UserRole userRole = UserRole.valueOf(dto.getRole());

       User user = User.builder()
               .email(dto.getEmail())
               .pwd(pwd)
               .nickname(dto.getNickname())
               .role(userRole)
               .reportNo(0)
               .build();

       User userSave=userRepository.save(user);

        return userSave.getUid();
    }

    /** 이메일 중복체크 */
    public boolean findEmailCheck(String email) {
        User findUser = userRepository.findByEmail(email);
        return findUser!=null;
    }

    /** 닉네임 중복체크 */
    public boolean findNicknameCheck(String nickname) {
        User findUser = userRepository.findByNickname(nickname);
        return findUser!=null;
    }
    /** authentication email 로 유저정보 받기 (sessionId로 유저정보 찾는 개념)  */
    @Override
    public UserDTO findUserByEmail(String sessionId) {
        User user = userRepository.findByEmail(sessionId);
        if(user!=null)
            return modelMapper.map(user,UserDTO.class);
        return null;
    }
}
