package com.momsway.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportDTO {
    private Long rid;
    private String comment;
    private Boolean status;
    private Long uid;
    private Long eid;
}
