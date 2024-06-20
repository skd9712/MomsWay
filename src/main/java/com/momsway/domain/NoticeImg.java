package com.momsway.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="notice_img")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class NoticeImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long iid;
    @Column(name="img_path", nullable = false)
    private String imgPath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="nid")
    private Notice notice;
}
