package com.momsway.dto;

import lombok.*;

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
    private String readNo;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private List<String> imgPaths;
}
