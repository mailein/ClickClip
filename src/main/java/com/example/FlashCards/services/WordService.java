package com.example.FlashCards.services;

import com.example.FlashCards.DTOs.WordDTO;
import com.example.FlashCards.configurations.Entity2DTOMapper;
import com.example.FlashCards.entities.Glossary;
import com.example.FlashCards.entities.Word;
import com.example.FlashCards.exceptions.NotFoundException;
import com.example.FlashCards.repositories.GlossaryRepository;
import com.example.FlashCards.repositories.WordRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class WordService {

    @Autowired
    GlossaryRepository glossaryRepository;
    @Autowired
    WordRepository wordRepository;

    @Autowired
    Entity2DTOMapper modelMapper;

    public List<WordDTO> getAllWords(Long glossaryId) {
        Glossary glossary = glossaryRepository.findById(glossaryId)
                .orElseThrow(() -> new NotFoundException("Glossary with id " + glossaryId + " is not found."));

        List<Word> foundWords = wordRepository.findByGlossaryId(glossary.getId());
        return foundWords.stream()
                .map(word -> modelMapper.map(word, WordDTO.class))
                .collect(Collectors.toList());
    }

    public WordDTO getWordById(Long wordId) {
        Word foundWord = wordRepository.findById(wordId)
                .orElseThrow(() -> new NotFoundException("Word with id " + wordId + " is not found."));
        return modelMapper.map(foundWord, WordDTO.class);
    }

    public WordDTO addWord(Long glossaryId, WordDTO wordDTO) {
        Glossary glossary = glossaryRepository.findById(glossaryId)
                .orElseThrow(() -> new NotFoundException("Glossary with id " + glossaryId + " is not found."));

        Word word = modelMapper.map(wordDTO, Word.class);
        word.setGlossary(glossary);

        Word savedWord = wordRepository.save(word);

        return modelMapper.map(savedWord, WordDTO.class);
    }

    public WordDTO updateWord(WordDTO wordDTO, Long wordId) {
        Word existingWord = wordRepository.findById(wordId)
                .orElseThrow(() -> new NotFoundException("Word with id " + wordId + " is not found."));

        existingWord.setName(wordDTO.getName());
        Word savedWord = wordRepository.save(existingWord);
        return modelMapper.map(savedWord, WordDTO.class);
    }

    public void deleteWord(Long wordId) {
        wordRepository.deleteById(wordId);
    }

    public void deleteAllWordInGlossary(Long glossaryId) {
        wordRepository.deleteAllByGlossaryId(glossaryId);
    }
}
