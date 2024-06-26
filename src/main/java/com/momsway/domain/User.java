package com.momsway.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="user")
@Getter
@NoArgsConstructor
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String pwd;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;
    @Column(nullable = false)
    private String nickname;
    @Column(name="report_no",nullable = false)
    @ColumnDefault("0")
    private Integer reportNo;

    @Builder
    public User(Long uid, String email, String pwd, UserRole role, String nickname, Integer reportNo){
        this.uid=uid;
        this.email=email;
        this.pwd=pwd;
        this.role=role;
        this.nickname=nickname;
        this.reportNo=reportNo;
    }

    @OneToMany(mappedBy = "academyUser")
    private List<Academy> academies = new ArrayList<>();

    @OneToMany(mappedBy = "entExamUser")
    private List<EntExam> entExams = new ArrayList<>();

    @OneToMany(mappedBy = "likeUser")
    private List<EntLike> userLikes = new ArrayList<>();
//
//    public void appendLike(EntLike like){
//        userLikes.add(like);
//        like.setLikeUser(this);
//    }

    @OneToMany(mappedBy = "entReplyUser")
    private List<EntReply> entReplies = new ArrayList<>();

    @OneToMany(mappedBy = "reportUser")
    private List<Report> reports = new ArrayList<>();


    public void setReportNo(Integer reportNo) {
        this.reportNo = reportNo;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
