package com.momsway.dto;

import com.momsway.domain.UserRole;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long uid;
    private String email;
    private String pwd;
    private String role;
    private String nickname;
    private Integer reportNo;


}
