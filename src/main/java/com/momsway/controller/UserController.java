package com.momsway.controller;

import com.momsway.dto.UserDTO;
import com.momsway.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;


    /** 로그인 페이지 */
    @GetMapping("/login")
    public String login(@RequestParam(name = "error", required = false) String error, Model model){
        if(error!=null){
            model.addAttribute("loginError", "loginError");
        }
        return "user/login";
    }

    /** 회원가입 페이지 */
    @GetMapping("/join")
    public String join(Model model){
        model.addAttribute("userDTO", new UserDTO());
        return "user/join";
    }

    /** 회원가입 결과 */
    @PostMapping("/join")
    public String joinResult(@Valid @ModelAttribute UserDTO dto
            , BindingResult bindingResult
            , Model model){

        boolean emailCheck = userService.findEmailCheck(dto.getEmail());
        boolean nicknameCheck = userService.findNicknameCheck(dto.getNickname());
//        log.info(".....이메일 중복확인 : {}", emailCheck);
//        log.info(".....닉네임 중복확인 : {}", nicknameCheck);
        if(emailCheck||nicknameCheck){
            model.addAttribute("joinError", "joinError");
            model.addAttribute("dto", dto);
            return "user/join";
        } else if(bindingResult.hasErrors()){
            return "user/join";
        }

        Long uid = userService.joinUser(dto);

        return "redirect:/login";
    }

    /** 이메일 중복체크 */
    @GetMapping("/checkEmail")
    public @ResponseBody String checkEmail(@RequestParam(required = false) String email){
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
