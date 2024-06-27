package com.momsway.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.modelmapper.internal.bytebuddy.implementation.bind.annotation.Empty;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AcademyDTO {
    private Long aid;
    //@Size(min =3, message = "nullable = false")
    private String title;
    private String content;
    private Long readNo;
    private LocalDateTime createAt;
   // @Size(max=5,message = "최대 5개의 이미지만 등록할 수 있습니다.")
    private List<MultipartFile> files;
    private List<String> imgPaths;
    private String imgPath;
    private String email;
    private String nickname;

    public AcademyDTO(Long aid, String title, Long readNo, LocalDateTime createAt, String nickname){
        this.aid=aid;
        this.title=title;
        this.readNo=readNo;
        this.createAt=createAt;
        this.nickname=nickname;
    }
}
