package com.momsway.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="notice")
@NoArgsConstructor
public class Notice extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nid;
    @Column(nullable = false)
    private Boolean notify;
    @Column(nullable = false)
    private String category;

    @Builder
    public Notice(Long nid, String title, String content, Long readNo, LocalDateTime createAt, LocalDateTime updateAt
    ,Boolean notify, String category){
        super(title,content,readNo,createAt,updateAt);
        this.nid=nid;
        this.notify=notify;
        this.category=category;
    }

    @OneToMany(mappedBy = "notice")
    private List<NoticeImg> noticeImgs = new ArrayList<>();

}
