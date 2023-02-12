package com.example.ClickClip.services;

import com.example.ClickClip.DTOs.GlossaryDTO;
import com.example.ClickClip.configurations.Entity2DTOMapper;
import com.example.ClickClip.entities.Glossary;
import com.example.ClickClip.entities.User;
import com.example.ClickClip.exceptions.NotFoundException;
import com.example.ClickClip.repositories.GlossaryRepository;
import com.example.ClickClip.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
public class GlossaryService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    GlossaryRepository glossaryRepository;

    @Autowired
    Entity2DTOMapper modelMapper;

    public List<GlossaryDTO> getAllGlossaries(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " is not found."));

        List<Glossary> foundGlossaries = glossaryRepository.findByUserId(user.getId());
        return foundGlossaries.stream()
                .map(glossary -> modelMapper.map(glossary, GlossaryDTO.class))
                .collect(Collectors.toList());
    }

    public GlossaryDTO getGlossaryById(Long glossaryId) {
        Glossary foundGlossary = glossaryRepository.findById(glossaryId)
                .orElseThrow(() -> new NotFoundException("Glossary with id " + glossaryId + " is not found."));
        return modelMapper.map(foundGlossary, GlossaryDTO.class);
    }

    public GlossaryDTO addGlossary(Long userId, GlossaryDTO glossaryDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " is not found."));

        Glossary glossary = modelMapper.map(glossaryDTO, Glossary.class);
        glossary.setUser(user);

        Glossary savedGlossary = glossaryRepository.save(glossary);

        return modelMapper.map(savedGlossary, GlossaryDTO.class);
    }

    // This method calls GlossaryRepository twice, should indeed be @Transactional
    public GlossaryDTO updateGlossary(GlossaryDTO glossaryDTO, Long glossaryId) {
        Glossary existingGlossary = glossaryRepository.findById(glossaryId)
                .orElseThrow(() -> new NotFoundException("Glossary with id " + glossaryId + " is not found."));

        existingGlossary.setName(glossaryDTO.getName());
        Glossary savedGlossary = glossaryRepository.save(existingGlossary);
        return modelMapper.map(savedGlossary, GlossaryDTO.class);
    }

    public void deleteGlossary(Long glossaryId) {
        glossaryRepository.deleteById(glossaryId);
    }

    public void deleteAllGlossariesByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id " + userId + " is not found."));
        List<Glossary> foundGlossaries = glossaryRepository.findByUserId(user.getId());
        foundGlossaries.forEach(glossary -> glossaryRepository.deleteById(glossary.getId()));
    }
}
