package com.momsway.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntExamDTO {
    private Long eid;
    private String imgPath;
    private Long uid;
    private String title;
    private String content;
    private Long readNo;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private String nickname;
}
