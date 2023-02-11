package com.example.ClickClip.services;

import com.example.ClickClip.DTOs.GlossaryDTO;
import com.example.ClickClip.DTOs.UserDTO;
import com.example.ClickClip.repositories.GlossaryRepository;
import com.example.ClickClip.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

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

    UserDTO u1;
    GlossaryDTO g1;
    GlossaryDTO g2;

    @BeforeEach
    public void setUp() {
        u1 = new UserDTO();
        u1.setName("u1");
        u1.setPassword("p1");

        g1 = new GlossaryDTO();
        g1.setName("g1");
        g2 = new GlossaryDTO();
        g2.setName("g2");
    }

    @AfterEach
    public void cleanUp() {
        //remove glossary before user, because glossary table has a foreign key of user_id
        glossaryRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void getGlossaryById_success() {
        UserDTO userDTO = userService.add(u1);
        GlossaryDTO expectedGlossaryDTO = glossaryService.addGlossary(userDTO.getId(), g1);

        GlossaryDTO testGlossaryDTO = glossaryService.getGlossaryById(expectedGlossaryDTO.getId());
        assertEquals(expectedGlossaryDTO.getId(), testGlossaryDTO.getId());
        assertEquals(expectedGlossaryDTO.getName(), testGlossaryDTO.getName());
        assertEquals(expectedGlossaryDTO.getUserDTO(), testGlossaryDTO.getUserDTO());
    }

    @Test
    public void getAllGlossaries_success(){
        UserDTO userDTO = userService.add(u1);
        GlossaryDTO expectedGlossaryDTO1 = glossaryService.addGlossary(userDTO.getId(), g1);
        GlossaryDTO expectedGlossaryDTO2 = glossaryService.addGlossary(userDTO.getId(), g2);

        List<GlossaryDTO> testGlossaryDTO = glossaryService.getAllGlossaries(userDTO.getId());
        assertEquals(2, testGlossaryDTO.size());
        assertEquals(expectedGlossaryDTO1, testGlossaryDTO.get(0));
        assertEquals(expectedGlossaryDTO2, testGlossaryDTO.get(1));
    }
}
