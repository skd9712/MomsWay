package com.momsway.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AcademyController {
    @GetMapping("/academy")
    public String academy(){
        return "academy";
    }

    @GetMapping("/academy/uid")
    public String detail(){
        return "acadetail";
    }
}
