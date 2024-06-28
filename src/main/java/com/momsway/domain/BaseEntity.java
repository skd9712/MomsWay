package com.momsway.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
    @Column(nullable = false)
    private String title;
    @Column(length = 1000, nullable = false)
    private String content;
    @Column(name = "read_no")
    private Long readNo;
    @Column(name="create_at" ,nullable = false)
    @CreatedDate
    private LocalDateTime createAt;
}
