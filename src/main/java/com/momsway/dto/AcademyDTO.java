package com.momsway.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AcademyDTO {
    private Long aid;
    private String title;
    private String content;
    private Long readNo;
    private LocalDateTime createAt;
    private String imgPath;
    private String nickname;
}
