package com.example.ClickClip.services;

import com.example.ClickClip.entities.Glossary;
import com.example.ClickClip.entities.User;
import com.example.ClickClip.exceptions.NotFoundException;
import com.example.ClickClip.repositories.GlossaryRepository;
import com.example.ClickClip.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class GlossaryService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    GlossaryRepository glossaryRepository;

    public Set<Glossary> getAllGlossaries(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " is not found."));

        return user.getGlossaries();
    }

    public Glossary getGlossaryById(Long glossaryId) {
        return glossaryRepository.findById(glossaryId)
                .orElseThrow(() -> new NotFoundException("Glossary with id " + glossaryId + " is not found."));
    }

    public Glossary addGlossary(Long userId, Glossary glossary) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " is not found."));
        glossary.setUser(user);
        return glossaryRepository.save(glossary);

        // TODO: save on the user side???
    }

    public Glossary updateGlossary(Glossary glossary, Long glossaryId) {
        Glossary existingGlossary = glossaryRepository.findById(glossaryId)
                .orElseThrow(() -> new NotFoundException("Glossary with id " + glossaryId + " is not found."));

        existingGlossary.setName(glossary.getName());
        existingGlossary.setWords(glossary.getWords());
        return glossaryRepository.save(existingGlossary);
    }

    public void deleteGlossary(Long glossaryId){
        glossaryRepository.deleteById(glossaryId);
    }
}
