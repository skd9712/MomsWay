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
@Table(name="ent_exam")
@NoArgsConstructor
@Getter
@Setter
public class EntExam extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eid;
    @Column(name="img_path")
    private String imgPath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="uid")
    private User entExamUser;

    @Builder
    public EntExam(Long eid, String imgPath, User entExamUser
            , String title , String content , Long readNo , LocalDateTime createAt){
        super(title, content, readNo, createAt);
        this.eid=eid;
        this.imgPath=imgPath;
        this.entExamUser=entExamUser;
    }

    @OneToMany(mappedBy = "likeEntExam", orphanRemoval = true)
    private List<EntLike> entExamEntLikes = new ArrayList<>();

    @OneToMany(mappedBy = "reportEntExam",orphanRemoval = true)
    private List<Report> entExamReports = new ArrayList<>();

    @OneToMany(mappedBy = "replyEntExam",orphanRemoval = true)
    private List<EntReply> entExamReplies = new ArrayList<>();
}