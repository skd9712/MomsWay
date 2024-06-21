package com.momsway.controller;

import com.momsway.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    /** 로그인 페이지 */
    @GetMapping("/login")
    public String login(){
        return "user/login";
    }

    /** 로그인 결과 */
    @PostMapping("/login_result")
    public String loginResult(){
        return "redirect:/main";
    }

    /** 회원가입 페이지 */
    @GetMapping("/join")
    public String join(){
        return "user/join";
    }
}
