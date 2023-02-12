package com.example.ClickClip.repositories;

import com.example.ClickClip.entities.Glossary;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GlossaryRepository extends JpaRepository<Glossary, Long> {

    @EntityGraph(value = "Glossary.user")
    public Optional<Glossary> findById(Long id);
    @EntityGraph(value = "Glossary.user")
    public List<Glossary> findByUserId(Long id);

    @EntityGraph(value = "Glossary.user")
    public void deleteAllByUserId(Long id);
}
