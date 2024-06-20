package com.momsway.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name="academy")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Academy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aid;
    @Column(nullable = false)
    private String title;
    @Column(length = 1000, nullable = false)
    private String content;
    @Column(name = "read_no", nullable = false)
    private Long readNo;
    @Column(name="create_at" , nullable = false)
    @CreatedDate
    private LocalDateTime createAt;
    @Column(name="img_path",length = 1000)
    private String imgPath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="uid")
    private User academyUser;

}
