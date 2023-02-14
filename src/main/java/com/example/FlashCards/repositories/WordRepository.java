package com.example.FlashCards.repositories;

import com.example.FlashCards.entities.Word;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {

    @EntityGraph(value = "Word.glossary")
    public Optional<Word> findById(Long wordId);

    @EntityGraph(value = "Word.glossary")
    public List<Word> findByGlossaryId(Long glossaryId);

    @EntityGraph(value = "Word.glossary")
    public void deleteAllByGlossaryId(Long glossaryId);
}
