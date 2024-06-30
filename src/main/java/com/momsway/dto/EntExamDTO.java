package com.momsway.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

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
    //private LocalDateTime updateAt;
    private String email;
    private String nickname;
    private List<MultipartFile> files;
    private List<String> imgPaths;
}
