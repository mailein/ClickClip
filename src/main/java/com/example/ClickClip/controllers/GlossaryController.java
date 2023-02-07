package com.example.ClickClip.controllers;

import com.example.ClickClip.entities.Glossary;
import com.example.ClickClip.services.GlossaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("glossaries")
public class GlossaryController {
    @Autowired
    GlossaryService glossaryService;

    @GetMapping("/{userId}")
    public Set<Glossary> getAllGlossaries(@RequestParam("userId") Long userId) {
        return glossaryService.getAllGlossaries(userId);
    }

    @GetMapping("/{glossaryId}")
    public Glossary getGlossary(@PathVariable("glossaryId") Long glossaryId) {
        return glossaryService.getGlossaryById(glossaryId);
    }

    @PostMapping("/{userId}")
    public Glossary addGlossary(@RequestParam("userId") Long userId,
                                @RequestBody Glossary glossary) {
        return glossaryService.addGlossary(userId, glossary);
    }

    @PutMapping("/{glossaryId}")
    public Glossary updateGlossary(@RequestBody Glossary glossary,
                                   @RequestParam("glossaryId") Long glossaryId){
        return glossaryService.updateGlossary(glossary, glossaryId);
    }

    @DeleteMapping("/{glossaryId}")
    public void deleteGlossary(@RequestParam("glossaryId") Long glossaryId){
        glossaryService.deleteGlossary(glossaryId);
    }
}
