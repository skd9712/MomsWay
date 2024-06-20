package com.momsway.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="ent_like")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class EntLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="uid")
    private User likeUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="eid")
    private EntExam likeEntExam;
}
