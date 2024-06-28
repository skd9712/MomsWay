package com.momsway.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeDTO {
    private Long nid;
    private Boolean notify;
    private String category;
    private String title;
    private String content;
    private Long readNo;
    private LocalDateTime createAt;
    //private LocalDateTime updateAt;
    private List<MultipartFile> files;
    private List<String> imgPaths;

    public NoticeDTO(Long nid, Boolean notify, String category,
                     String title,Long readNo, LocalDateTime createAt){
        this.nid=nid;
        this.notify=notify;
        this.category=category;
        this.title=title;
        this.readNo=readNo;
        this.createAt=createAt;

    }
}
