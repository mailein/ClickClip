package com.example.FlashCards.controllers;

import com.example.FlashCards.DTOs.WordDTO;
import com.example.FlashCards.services.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/words")
public class WordController {

    @Autowired
    WordService wordService;

    @GetMapping("/glossary/{glossaryId}")
    public ResponseEntity<List<WordDTO>> getAllWords(@PathVariable("glossaryId") Long glossaryId) {
        return new ResponseEntity<>(wordService.getAllWords(glossaryId), HttpStatus.OK);
    }

    @GetMapping("/{wordId}")
    public ResponseEntity<WordDTO> getWord(@PathVariable("wordId") Long wordId) {
        return new ResponseEntity<>(wordService.getWordById(wordId), HttpStatus.OK);
    }

    @PostMapping("/glossary/{glossaryId}")
    public ResponseEntity<WordDTO> addWord(@PathVariable("glossaryId") Long glossaryId,
                                           @RequestBody WordDTO wordDTO) {
        return new ResponseEntity<>(wordService.addWord(glossaryId, wordDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{wordId}")
    public ResponseEntity<WordDTO> updateWord(@RequestBody WordDTO wordDTO,
                                              @PathVariable("wordId") Long wordId) {
        WordDTO updatedWord = wordService.updateWord(wordDTO, wordId);
        return new ResponseEntity<>(updatedWord, HttpStatus.OK);
    }

    @DeleteMapping("/{wordId}")
    public ResponseEntity<String> deleteWord(@PathVariable("wordId") Long wordId) {
        wordService.deleteWord(wordId);
        return new ResponseEntity<>("Deleted word with id " + wordId, HttpStatus.OK);
    }

    @DeleteMapping("/glossary/{glossaryId}")
    public ResponseEntity<String> deleteAllWords(@PathVariable("glossaryId") Long glossaryId) {
        wordService.deleteAllWordInGlossary(glossaryId);
        return new ResponseEntity<>("Deleted all words in glossary with id " + glossaryId, HttpStatus.OK);
    }
}
