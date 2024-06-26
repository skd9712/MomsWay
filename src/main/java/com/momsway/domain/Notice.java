package com.momsway.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="notice")
@NoArgsConstructor
@Getter @Setter
public class Notice extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nid;
    @Column(nullable = false)
    private Boolean notify;
    //@Enumerated(EnumType.STRING)
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

    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL)
    private List<NoticeImg> noticeImgs = new ArrayList<>();

}
