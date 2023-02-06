package com.example.ClickClip.entities;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.time.Instant;
import java.util.LinkedHashSet;

@Slf4j
@MappedSuperclass
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class AbstractEntity implements Serializable {

    @NotNull
    @Column(name = "name", unique = true)
    private String name;

    @NotNull
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @NotNull
    @Column(name = "update_at")
    private Instant updateAt;


    @PrePersist
    public void prePersist() {
        Instant now = Instant.now();
        setCreatedAt(now);
        setUpdateAt(now);
    }

    @PreUpdate
    public void preUpdate() {
        setUpdateAt(Instant.now());
    }
}
