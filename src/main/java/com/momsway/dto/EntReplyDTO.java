package com.momsway.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntReplyDTO {
    private Long repId;
    private String content;
    private LocalDateTime createAt;
    private Long uid;
    private Long eid;
}
