package com.example.FlashCards.configurations;

import com.example.FlashCards.DTOs.GlossaryDTO;
import com.example.FlashCards.DTOs.WordDTO;
import com.example.FlashCards.entities.Glossary;
import com.example.FlashCards.entities.Word;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@Getter
public class Entity2DTOMapper extends ModelMapper{

    public Entity2DTOMapper() {
        this.typeMap(Glossary.class, GlossaryDTO.class).addMappings(mapper -> {
            mapper.map(Glossary::getUser,
                    GlossaryDTO::setUserDTO);
        });
        this.typeMap(Word.class, WordDTO.class).addMappings(mapper -> {
            mapper.map(Word::getGlossary,
                    WordDTO::setGlossaryDTO);
        });
    }
}
