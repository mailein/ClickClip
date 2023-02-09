package com.example.ClickClip.controllers;

import com.example.ClickClip.DTOs.GlossaryDTO;
import com.example.ClickClip.entities.Glossary;
import com.example.ClickClip.services.GlossaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("glossaries")
public class GlossaryController {
    @Autowired
    GlossaryService glossaryService;

    @GetMapping("/{userId}")
    public ResponseEntity<Set<GlossaryDTO>> getAllGlossaries(@RequestParam("userId") Long userId) {
        return new ResponseEntity<>(glossaryService.getAllGlossaries(userId), HttpStatus.OK);
    }

    @GetMapping("/{glossaryId}")
    public ResponseEntity<GlossaryDTO> getGlossary(@PathVariable("glossaryId") Long glossaryId) {
        return new ResponseEntity<>(glossaryService.getGlossaryById(glossaryId), HttpStatus.OK);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<GlossaryDTO> addGlossary(@RequestParam("userId") Long userId,
                                                   @RequestBody GlossaryDTO glossaryDTO) {
        return new ResponseEntity<>(glossaryService.addGlossary(userId, glossaryDTO), HttpStatus.OK);
    }

    @PutMapping("/{glossaryId}")
    public ResponseEntity<GlossaryDTO> updateGlossary(@RequestBody GlossaryDTO glossaryDTO,
                                                   @RequestParam("glossaryId") Long glossaryId) {
        return new ResponseEntity<>(glossaryService.updateGlossary(glossaryDTO, glossaryId), HttpStatus.OK);
    }

    @DeleteMapping("/{glossaryId}")
    public ResponseEntity<String> deleteGlossary(@RequestParam("glossaryId") Long glossaryId) {
        glossaryService.deleteGlossary(glossaryId);
        return new ResponseEntity<>("Deleted glossary with id " + glossaryId, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteAllGlossaries(@RequestParam("userId") Long userId){
        glossaryService.deleteAllGlossariesByUser(userId);
        return new ResponseEntity<>("Deleted all glossaries from user with id " + userId, HttpStatus.OK);
    }
}
