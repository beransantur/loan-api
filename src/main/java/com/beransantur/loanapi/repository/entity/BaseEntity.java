package com.beransantur.loanapi.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    protected Integer id;

    @Column(name = "created_at")
    @CreatedDate
    protected LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "modified_at")
    @LastModifiedDate
    protected LocalDateTime modifiedAt;
}
