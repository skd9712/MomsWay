package com.momsway.service;

import com.momsway.domain.User;
import com.momsway.domain.UserRole;
import com.momsway.dto.EntExamDTO;
import com.momsway.dto.UserDTO;
import com.momsway.repository.like.LikeRepository;
import com.momsway.repository.user.UserRepository;
import groovy.util.logging.Slf4j;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final PasswordEncoder encoder;
    private final ModelMapper modelMapper;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    /** 회원가입 */
   @Override
    public Long joinUser(UserDTO dto) {
       // 비밀번호 암호화
       String pwd = encoder.encode(dto.getPwd());

       // Role
       UserRole userRole = UserRole.valueOf(dto.getRole());

       boolean findEmail = findEmailCheck(dto.getEmail());
       boolean findNickname = findNicknameCheck(dto.getNickname());

       if(findEmail || findNickname)
           throw new RuntimeException("이미 사용 중입니다.");

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

    /** authentication email 로 유저id 받기 */
    @Override
    public long findUidByEmail(String sessionId) {
        User user = userRepository.findByEmail(sessionId);
        long result = 0;
        if(user!=null)
            result = user.getUid();
        return result;
    }

    @Override
    public List<EntExamDTO> myentlist(String username) {
        long uidByEmail = findUidByEmail(username);
        List<EntExamDTO> myentlist = userRepository.myentlist(uidByEmail);
        return myentlist;
    }

    @Override
    public List<EntExamDTO> findByUid(String username) {
        long uidByEmail = findUidByEmail(username);
        List<EntExamDTO> likeTitles = likeRepository.findByUid(uidByEmail);
        return likeTitles;
    }

    @Override
    public UserDTO findUserInfo(String username) {
        long uidByEmail = findUidByEmail(username);
        UserDTO user = userRepository.findByUserInfo(uidByEmail);
        return user;
    }


    @Override
    @Transactional
    public long userUpdate(Long uid,UserDTO dto) {
        log.info("Starting update for eid: {}", uid);
        User user = userRepository.findById(uid)
                .orElseThrow(() -> new IllegalArgumentException("invalid uid: " + uid));
        user.setEmail(dto.getEmail());
        user.setNickname(dto.getNickname());
        User save = userRepository.save(user);
        return save.getUid();
    }
}
