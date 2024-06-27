package com.momsway.controller;

import com.momsway.dto.AcademyDTO;
import com.momsway.dto.EntExamDTO;
import com.momsway.dto.UserDTO;
import com.momsway.service.EntExamService;
import com.momsway.service.LikeService;
import com.momsway.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MyPageController {
    private final LikeService likeService;
    private final UserService userService;
    private final EntExamService entExamService;
    @GetMapping("/mypage")
    public String mypage(Model model, Principal principal){
        String username = principal.getName();
        List<EntExamDTO> myentlist = userService.myentlist(username);
        model.addAttribute("myentlist",myentlist);
        List<AcademyDTO> myacalist = userService.myacalist(username);
        model.addAttribute("myacalist",myacalist);
        List<EntExamDTO> likeTitles = userService.findByUid(username);
        model.addAttribute("likeTitles",likeTitles);
        return "user/mypage";
    }
    @GetMapping("/myinfo")
    public String myinfo(Model model, Principal principal){
        String username = principal.getName();
//        long uidByEmail = userService.findUidByEmail(username);
        UserDTO dto = userService.findUserInfo(username);
        model.addAttribute("dto",dto);
        return "user/myinfo";
    }
    @PostMapping("/myinfo")
    public String myinfoupdate(Long uid,@ModelAttribute("dto") UserDTO dto, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "user/myinfo";
        }else {
            long id = userService.userUpdate(uid,dto);
            return "redirect:/main";
        }
    }
}
