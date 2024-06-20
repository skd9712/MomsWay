package com.momsway.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeDTO {
    private Long lid;
    private Long uid;
    private Long eid;
}
