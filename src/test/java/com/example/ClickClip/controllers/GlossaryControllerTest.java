package com.example.ClickClip.controllers;

import com.example.ClickClip.DTOs.GlossaryDTO;
import com.example.ClickClip.configurations.Entity2DTOMapper;
import com.example.ClickClip.entities.Glossary;
import com.example.ClickClip.entities.User;
import com.example.ClickClip.exceptions.NotFoundException;
import com.example.ClickClip.services.GlossaryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
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

    Entity2DTOMapper modelMapper = new Entity2DTOMapper();

    User user;
    Glossary g1WithoutUser;
    Glossary g1;
    Glossary g1Updated;
    Glossary g2;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .id(1L)//this user is not saved to repository, so set the id here
                .name("username")
//                .password("password")//the returned UserDTO has no JSON key password, just mock it here.
                .build();
        g1WithoutUser = Glossary.builder()
                .id(1L)//this glossary is not saved to repository, so set the id here
                .name("g1")
                .build();
        g1 = Glossary.builder()
                .id(1L)//this glossary is not saved to repository, so set the id here
                .name("g1")
                .user(user)
                .build();
        g1Updated = Glossary.builder()
                .id(1L)//this glossary is not saved to repository, so set the id here
                .name("g2")
                .user(user)
                .build();
        g2 = Glossary.builder()
                .id(2L)//this glossary is not saved to repository, so set the id here
                .name("g2")
                .user(user)
                .build();
    }

    @Test
    @DisplayName("GET /glossaries/{glossaryId} calls GlossaryService.getGlossaryById{glossaryId} and returns the correct glossaryDTO with userDTO")
    public void getGlossaryById_success() throws Exception {
        when(glossaryService.getGlossaryById(g1.getId()))
                .thenReturn(modelMapper.map(g1, GlossaryDTO.class));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/glossaries" + "/" + g1.getId())
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(g1.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(g1.getName())))
                .andExpect(jsonPath("$.userDTO.id", is(g1.getUser().getId()), Long.class))
                .andExpect(jsonPath("$.userDTO.name", is(g1.getUser().getName())));
        verify(glossaryService).getGlossaryById(g1.getId());
    }

    @Test
    @DisplayName("GET /glossaries/user/{userId} calls GlossaryService.getAllGlossaries(userId) and returns all GlossaryDTOs of UserDTO with userid")
    public void getAllGlossariesOfUser_success() throws Exception {
        List<GlossaryDTO> glossaryDTOList =
                Stream.of(g1, g2)
                        .map(glossary -> modelMapper.map(glossary, GlossaryDTO.class))
                        .collect(Collectors.toList());
        when(glossaryService.getAllGlossaries(user.getId()))
                .thenReturn(glossaryDTOList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/glossaries" + "/user" + "/" + user.getId())
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(glossaryDTOList.size())))
                .andExpect(jsonPath("$[0].id", is(g1.getId()), Long.class))
                .andExpect(jsonPath("$[1].id", is(g2.getId()), Long.class))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder(g1.getName(), g2.getName())))
                .andExpect(jsonPath("$[0].userDTO.id", is(g1.getUser().getId()), Long.class))
                .andExpect(jsonPath("$[1].userDTO.id", is(g2.getUser().getId()), Long.class))
                .andExpect(jsonPath("$[*].userDTO.name", equalTo(List.of(g1.getUser().getName(), g2.getUser().getName()))));
        verify(glossaryService).getAllGlossaries(user.getId());
    }

    @Test
    @DisplayName("POST /glossaries/user/{userId} with GlossaryDTO calls GlossaryService.addGlossary(userId) and returns the saved GlossaryDTO of UserDTO with userid")
    public void postGlossaryOfUser_success() throws Exception {
        GlossaryDTO g1WithoutUserDTO = modelMapper.map(g1WithoutUser, GlossaryDTO.class);
        GlossaryDTO g1DTO = modelMapper.map(g1, GlossaryDTO.class);

        when(glossaryService.addGlossary(user.getId(), g1WithoutUserDTO))
                .thenReturn(g1DTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/glossaries" + "/user" + "/" + user.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(g1WithoutUserDTO))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.id", is(g1DTO.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(g1DTO.getName())))
                .andExpect(jsonPath("$.userDTO.id", is(g1DTO.getUserDTO().getId()), Long.class))
                .andExpect(jsonPath("$.userDTO.name", is(g1DTO.getUserDTO().getName())));
        verify(glossaryService).addGlossary(user.getId(), g1WithoutUserDTO);
    }

    @Test
    @DisplayName("PUT /glossaries/{glossaryId} with GlossaryDTO calls GlossaryService.updateGlossary(GlossaryDTO, glossaryId) and returns the updated GlossaryDTO of UserDTO with the same userid")
    public void putGlossaryOfUser_success() throws Exception {
        GlossaryDTO g1DTO = modelMapper.map(g1, GlossaryDTO.class);
        GlossaryDTO g2DTO = modelMapper.map(g2, GlossaryDTO.class);
        GlossaryDTO g1UpdatedDTO = modelMapper.map(g1Updated, GlossaryDTO.class);

        //the GlossaryDTO passed in as parameter and as return value shouldn't have UserDTO.password
        when(glossaryService.updateGlossary(g2DTO, g1DTO.getId()))
                .thenReturn(g1UpdatedDTO);

        //UserDTO in GlossaryDTO has the JSON key password can cause problem in mock test
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/glossaries" + "/" + g1DTO.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(g2DTO))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.id", is(g1UpdatedDTO.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(g1UpdatedDTO.getName())))
                .andExpect(jsonPath("$.userDTO.id", is(g1UpdatedDTO.getUserDTO().getId()), Long.class))
                .andExpect(jsonPath("$.userDTO.name", is(g1UpdatedDTO.getUserDTO().getName())));
        verify(glossaryService).updateGlossary(g2DTO, g1DTO.getId());
    }

    @Test
    @DisplayName("DELETE /glossaries/{glossaryId} with GlossaryDTO calls GlossaryService.deleteGlossaryById(glossaryId)")
    public void deleteGlossaryById_success() throws Exception {
        GlossaryDTO g1DTO = modelMapper.map(g1, GlossaryDTO.class);

        //the GlossaryDTO passed in as parameter and as return value shouldn't have UserDTO.password
        when(glossaryService.getGlossaryById(g1DTO.getId()))
                .thenReturn(g1DTO)
                .thenThrow(new NotFoundException("Glossary with id " + g1DTO.getId() + " is not found."));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/glossaries" + "/" + g1DTO.getId())
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(g1DTO.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(g1DTO.getName())))
                .andExpect(jsonPath("$.userDTO.id", is(g1DTO.getUserDTO().getId()), Long.class))
                .andExpect(jsonPath("$.userDTO.name", is(g1DTO.getUserDTO().getName())));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/glossaries" + "/" + g1DTO.getId())
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("Deleted glossary with id " + g1DTO.getId())));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/glossaries" + "/" + g1DTO.getId())
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
        // TODO: in "$", Body is not null, but rather Body =
        verify(glossaryService).deleteGlossary(g1DTO.getId());
        verify(glossaryService, times(2)).getGlossaryById(g1DTO.getId());
    }

    @Test
    @DisplayName("DELETE /glossaries/user/{userId} with GlossaryDTO calls GlossaryService.deleteAllGlossariesByUser(userId)")
    public void deleteGlossaryOfUser_success() throws Exception {
        GlossaryDTO g1DTO = modelMapper.map(g1, GlossaryDTO.class);
        GlossaryDTO g2DTO = modelMapper.map(g2, GlossaryDTO.class);
        List<GlossaryDTO> glossaryDTOList = List.of(g1DTO, g2DTO);

        //the GlossaryDTO passed in as parameter and as return value shouldn't have UserDTO.password
        when(glossaryService.getAllGlossaries(g1DTO.getId()))
                .thenReturn(glossaryDTOList)
                .thenReturn(List.of());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/glossaries" + "/user" + "/" + user.getId())
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(glossaryDTOList.size())));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/glossaries" + "/user" + "/" + user.getId())
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("Deleted all glossaries from user with id " + user.getId())));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/glossaries" + "/user" + "/" + user.getId())
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(glossaryService).deleteAllGlossariesByUser(user.getId());
    }
}
