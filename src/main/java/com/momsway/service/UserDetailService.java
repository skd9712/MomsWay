package com.momsway.service;

import com.momsway.domain.User;
import com.momsway.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

//        log.info("email....{}", email);

        User findUser
                = userRepository.findByEmail(email);

//        log.info("user........{}", findUser.getNickname()+", "+findUser.getEmail());
        if(findUser!=null)
            return new CustomUserDetails(findUser);

        return null;
    }
}
