package com.example.ClickClip.repositories;

import com.example.ClickClip.entities.Glossary;
import com.example.ClickClip.entities.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GlossaryRepository extends JpaRepository<Glossary, Long> {

    @EntityGraph(value = "Glossary.user")
    public Optional<Glossary> findById(Long id);
    public List<Glossary> findByUser(User user);
    public void deleteByUser(User user);
}
