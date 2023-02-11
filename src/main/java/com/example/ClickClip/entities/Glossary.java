package com.example.ClickClip.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "glossaries")
@NamedEntityGraph(name = "Glossary.user", attributeNodes = @NamedAttributeNode("user"))
public class Glossary extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "glossary_id", nullable = false, updatable = false)
    private Long id;

    //    @ToString.Exclude
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)//don't cascade from child to parent
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ToString.Exclude
    @OneToMany(mappedBy = "glossary", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Word> words;

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
