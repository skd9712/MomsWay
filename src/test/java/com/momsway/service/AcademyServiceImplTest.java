package com.momsway.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AcademyServiceImplTest {
    @Autowired
    private AcademyService academyService;
    @Test
    public void findByAid(){
        Assertions.assertThat(academyService.findByAid(1L).getImgPath()).isNull();
        Assertions.assertThat(academyService.findByAid(3L).getEmail()).isEqualTo("apass@test.com");
    }
}