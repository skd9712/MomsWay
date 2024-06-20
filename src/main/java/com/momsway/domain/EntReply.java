package com.momsway.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "ent_reply")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class EntReply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "repid")
    private Long repId;
    @Column(nullable = false)
    private String content;
    @Column(name="create_at", nullable = false)
    @CreatedDate
    private LocalDateTime createAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="uid")
    private User entReplyUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="eid")
    private EntExam replyEntExam;
}
