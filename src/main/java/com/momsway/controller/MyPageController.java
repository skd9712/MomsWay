package com.momsway.controller;

import com.momsway.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MyPageController {
    private final LikeService likeService;
    @GetMapping("/mypage/{uid}")
    public String mypage(@PathVariable Long uid, Model model){
        List<String> likeTitles = likeService.findByUid(uid);
        model.addAttribute("likeTitles",likeTitles);
        return "mypage";
    }
}
