package com.momsway.controller;

import com.momsway.dto.EntExamDTO;
import com.momsway.service.EntExamService;
import com.momsway.service.LikeService;
import com.momsway.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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
        List<EntExamDTO> likeTitles = userService.findByUid(username);
        model.addAttribute("likeTitles",likeTitles);
        return "user/mypage";
    }
}
