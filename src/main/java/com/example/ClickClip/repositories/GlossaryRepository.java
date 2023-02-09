package com.example.ClickClip.repositories;

import com.example.ClickClip.entities.Glossary;
import com.example.ClickClip.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GlossaryRepository extends JpaRepository<Glossary, Long> {
    public void deleteByUser(User user);
}
