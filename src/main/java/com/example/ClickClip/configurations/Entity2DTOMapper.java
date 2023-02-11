package com.example.ClickClip.configurations;

import com.example.ClickClip.DTOs.GlossaryDTO;
import com.example.ClickClip.entities.Glossary;
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

    }
}
