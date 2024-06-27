package com.momsway.dto;

import com.momsway.domain.UserRole;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long uid;
    @NotEmpty(message = "이메일을 입력하세요.")
    private String email;
    private String pwd;
    private String role;
    @NotEmpty(message = "닉네임을 입력하세요.")
    @Size(min = 1, max = 10, message = "닉네임은 1자 이상 15자 이하여야 합니다.")
    private String nickname;
    private Integer reportNo;
}
