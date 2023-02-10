package com.example.ClickClip.controllers;

import com.example.ClickClip.DTOs.GlossaryDTO;
import com.example.ClickClip.entities.Glossary;
import com.example.ClickClip.entities.User;
import com.example.ClickClip.services.GlossaryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = GlossaryController.class)
public class GlossaryControllerTest {
    @MockBean
    GlossaryService glossaryService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    ModelMapper modelMapper = new ModelMapper();

    @Test
    public void getGlossary_success() throws Exception {
        User expectedUser = User.builder()
//                .id(1L)//this user is not saved to repository, so set the id here
                .name("username")
                .password("password")
                .build();
        Glossary g = Glossary.builder()
                .id(1L)//this glossary is not saved to repository, so set the id here
                .name("glossaryname")
                .user(expectedUser)
                .build();
        when(glossaryService.getGlossaryById(g.getId()))
                .thenReturn(modelMapper.map(g, GlossaryDTO.class));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/glossaries/1")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(g.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(g.getName())))
                .andExpect(jsonPath("$.user.name", is(g.getUser().getName())));
        verify(glossaryService).getGlossaryById(g.getId());

    }
}
