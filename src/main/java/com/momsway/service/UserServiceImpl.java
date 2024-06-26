package com.momsway.service;

import com.momsway.domain.User;
import com.momsway.domain.UserRole;
import com.momsway.dto.EntExamDTO;
import com.momsway.dto.UserDTO;
import com.momsway.repository.like.LikeRepository;
import com.momsway.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final PasswordEncoder encoder;
    private final ModelMapper modelMapper;

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
    public Page<UserDTO> findUsers(Pageable pageable, String search
            , String search_txt) {

        int totalPage = userRepository.getCount(search, search_txt);

        List<User> userList = new ArrayList<>();
        if(search=="email"||"email".equals(search)) {
            userList=userRepository.findUsersEmail(pageable, search_txt);
        }else if(search=="nickname"||"nickname".equals(search)) {
            userList=userRepository.findUsersNick(pageable, search_txt);
        }else{
            userList=userRepository.findUsersNick(pageable, search_txt);
        }

        List<UserDTO> userDTOList = new ArrayList<>();
        if(userList.isEmpty()){
            return new PageImpl<>(userDTOList, pageable, totalPage);
        }

        userDTOList = userList.stream()
                .map(item -> modelMapper.map(item, UserDTO.class))
                .collect(Collectors.toList());

        return new PageImpl<>(userDTOList, pageable, totalPage);
    }

    @Override
    public UserDTO getUserDetail(Long uid) {
        User user = userRepository.getUserDetail(uid);
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        return userDTO;
    }

    @Override
    public Long deleteUser(Long uid) {
        userRepository.deleteById(uid);
        return uid;
    }
}
