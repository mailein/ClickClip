package com.example.ClickClip.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
public class Glossary extends AbstractEntity {
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ToString.Exclude
    @ManyToMany(mappedBy = "glossaries", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Word> words = new LinkedHashSet<>();
}
