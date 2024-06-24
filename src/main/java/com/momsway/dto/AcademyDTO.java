package com.momsway.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

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
    private List<MultipartFile> files;
    private String imgPath;
    private String nickname;

    public AcademyDTO(Long aid, String title, Long readNo, LocalDateTime createAt, String nickname){
        this.aid=aid;
        this.title=title;
        this.readNo=readNo;
        this.createAt=createAt;
        this.nickname=nickname;
    }
}
