package com.example.ClickClip.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "glossaries")
public class Glossary extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "glossary_id", nullable = false, updatable = false)
    private Long id;

    @ToString.Exclude
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)//don't cascade from child to parent
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

//    @NotNull
    @ToString.Exclude
    @OneToMany(mappedBy = "glossary", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Word> words;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Glossary glossary = (Glossary) o;
        return getId() != null && Objects.equals(getId(), glossary.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
