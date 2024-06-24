package com.momsway.controller;

import com.momsway.dto.UserDTO;
import com.momsway.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;


    /** 로그인 페이지 */
    @GetMapping("/login")
    public String login(){
        return "user/login";
    }

    /** 회원가입 페이지 */
    @GetMapping("/join")
    public String join(){
        return "user/join";
    }

    /** 회원가입 결과 */
    @PostMapping("/join")
    public String joinResult(UserDTO dto){
        Long uid = userService.joinUser(dto);

        return "redirect:/login";
    }

    /** 이메일 중복체크 */
    @GetMapping("/checkEmail")
    public @ResponseBody String checkEmail(@RequestParam(required = false) String email){
//        if(email==null || "".equals(email))
//            email="";
        boolean findEmail = userService.findEmailCheck(email);
        if(email==null || "".equals(email))
            return "null";
        else if(findEmail)
            return "true";
        else
            return "false";
    }

    /** 닉네임 중복체크 */
    @GetMapping("/checkNickname")
    public @ResponseBody String checkNickname(@RequestParam(required = false) String nickname){
        boolean findEmail = userService.findNicknameCheck(nickname);
        if(nickname==null || "".equals(nickname))
            return "null";
        else if(findEmail)
            return "true";
        else
            return "false";
    }
}
