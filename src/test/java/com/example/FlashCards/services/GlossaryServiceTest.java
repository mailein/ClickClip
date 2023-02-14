package com.example.FlashCards.services;

import com.example.FlashCards.DTOs.GlossaryDTO;
import com.example.FlashCards.configurations.Entity2DTOMapper;
import com.example.FlashCards.entities.Glossary;
import com.example.FlashCards.entities.User;
import com.example.FlashCards.repositories.GlossaryRepository;
import com.example.FlashCards.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
public class GlossaryServiceTest {

    @Autowired
    UserService userService;
    @Autowired
    GlossaryService glossaryService;

    @Autowired
    GlossaryRepository glossaryRepository;
    @Autowired
    private UserRepository userRepository;

    Entity2DTOMapper modelMapper = new Entity2DTOMapper();

    User user;
    User savedUser;
    Glossary g1;
    Glossary g2;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .name("u1")
                .password("p1")
                .build();
        savedUser = userRepository.save(user);

        g1 = Glossary.builder()
                .name("g1")
                .user(user)
                .build();
        g2 = Glossary.builder()
                .name("g2")
                .user(user)
                .build();
    }

    @AfterEach
    public void cleanUp() {
        //remove glossary before user, because glossary table has a foreign key of user_id
        glossaryRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Saved 1 user and 1 glossary to repo. Get glossary by glossary id.")
    public void getGlossaryById_success() {
        Glossary expectedGlossary = glossaryRepository.save(g1);
        GlossaryDTO expectedGlossaryDTO = modelMapper.map(expectedGlossary, GlossaryDTO.class);

        GlossaryDTO testGlossaryDTO = glossaryService.getGlossaryById(expectedGlossaryDTO.getId());

        assertEquals(expectedGlossaryDTO.getId(), testGlossaryDTO.getId());
        assertEquals(expectedGlossaryDTO.getName(), testGlossaryDTO.getName());
        assertEquals(expectedGlossaryDTO.getUserDTO(), testGlossaryDTO.getUserDTO());
    }

    @Test
    @DisplayName("Saved 1 user and 2 glossaries to repo. Get all glossaries of user.")
    public void getAllGlossaries_success() {
        Glossary expectedGlossary1 = glossaryRepository.save(g1);
        Glossary expectedGlossary2 = glossaryRepository.save(g2);
        List<GlossaryDTO> expectedGlossaryDTOList = Stream.of(expectedGlossary1, expectedGlossary2)
                .map(glossary -> modelMapper.map(glossary, GlossaryDTO.class))
                .toList();

        List<GlossaryDTO> testGlossaryDTO = glossaryService.getAllGlossaries(user.getId());

        assertEquals(expectedGlossaryDTOList.size(), testGlossaryDTO.size());
        assertEquals(expectedGlossaryDTOList.get(0), testGlossaryDTO.get(0));
        assertEquals(expectedGlossaryDTOList.get(1), testGlossaryDTO.get(1));
        assertEquals(expectedGlossaryDTOList.get(1).getUserDTO().getId(), testGlossaryDTO.get(1).getUserDTO().getId());
    }

    @Test
    @DisplayName("Saved user to repo. Add glossary for user.")
    public void postGlossaryOfUser_success() {
        GlossaryDTO testGlossaryDTO = glossaryService
                .addGlossary(user.getId(), modelMapper.map(g1, GlossaryDTO.class));

        List<GlossaryDTO> glossaryDTOList = glossaryRepository.findByUserId(user.getId())
                .stream()
                .map(glossary -> modelMapper.map(glossary, GlossaryDTO.class))
                .toList();

        assertThat(glossaryDTOList, hasItem(testGlossaryDTO));
    }

    @Test
    @DisplayName("Saved 1 user and 1 glossary to repo. Update that glossary with a new glossary.")
    // POSTMAN succeeds without @Transactional in the same operation, but test fails without @Transactional.
    // use GlossaryRepository to save a glossary with user, and it returns a glossary with null user
    public void putGlossary_success() {
        Glossary savedG1 = glossaryRepository.save(g1);
        GlossaryDTO updateWith = new GlossaryDTO();
        String newName = "new g";
        updateWith.setName(newName);
        GlossaryDTO testGlossaryDTO = glossaryService
                .updateGlossary(updateWith, savedG1.getId());

        assertThat(testGlossaryDTO.getId(), is(g1.getId()));
        assertThat(testGlossaryDTO.getName(), is(newName));
        assertThat(testGlossaryDTO.getUserDTO(), equalTo(modelMapper.map(g1, GlossaryDTO.class).getUserDTO()));
    }

    @Test
    @DisplayName("Saved 1 user and 1 glossary to repo. Delete that glossary.")
    public void deleteGlossary_success() {
        Glossary savedGlossary = glossaryRepository.save(g1);
        assertThat(userRepository.findAll(), hasSize(1));
        assertThat(glossaryRepository.findAll(), hasSize(1));
        assertThat(glossaryRepository.findById(savedGlossary.getId()).get(), equalTo(savedGlossary));

        glossaryService.deleteGlossary(savedGlossary.getId());

        assertThat(glossaryRepository.findAll(), hasSize(0));
        assertThat(userRepository.findAll(), hasSize(1));
    }

    @Test
    @DisplayName("Saved 1 user and 2 glossaries to repo. Delete all glossaries of the user.")
    public void deleteAllGlossaries_success(){
        Glossary savedGlossary1 = glossaryRepository.save(g1);
        Glossary savedGlossary2 = glossaryRepository.save(g2);
        assertThat(userRepository.findAll(), hasSize(1));
        assertThat(glossaryRepository.findAll(), hasSize(2));
        assertThat(glossaryRepository.findAll(), equalTo(List.of(savedGlossary1, savedGlossary2)));

        glossaryService.deleteAllGlossariesByUser(savedUser.getId());

        assertThat(glossaryRepository.findAll(), hasSize(0));
        assertThat(userRepository.findAll(), hasSize(1));
    }
}
