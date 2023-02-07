package com.example.ClickClip.controllers;

import com.example.ClickClip.DTOs.UserDTO;
import com.example.ClickClip.entities.Glossary;
import com.example.ClickClip.entities.User;
import com.example.ClickClip.entities.Word;
import com.example.ClickClip.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {
    @MockBean
    UserService userService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    ModelMapper modelMapper = new ModelMapper();

    @Test
    public void getAllUsers_success() throws Exception {
        User expectedUser = User.builder()
                .id(1L)
                .name("username")
                .password("password")
                .build();
        Glossary g = Glossary.builder()
                .id(1L)
                .name("glossaryname")
                .user(expectedUser)
                .words(new LinkedHashSet<>())
                .build();
        Word w = Word.builder()
                .id(1L)
                .name("wordname")
                .glossary(g)
                .build();

        g.getWords().add(w);
        expectedUser.setGlossaries(Set.of(g));

        when(userService.getAllUsers())
                .thenReturn(List.of(modelMapper.map(expectedUser, UserDTO.class)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(expectedUser.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(expectedUser.getName())));
//                .andExpect(jsonPath("$[0].password", is(expectedUser.getPassword())));//DTO doesn't include password
        verify(userService).getAllUsers();
    }
}
