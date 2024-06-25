package com.momsway.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="report")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rid;
    @Column(length = 1000, nullable = false)
    private String comment;
    @Column(nullable = false)
    private Boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="uid")
    private User reportUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="eid", nullable = false)
    private EntExam reportEntExam;

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
