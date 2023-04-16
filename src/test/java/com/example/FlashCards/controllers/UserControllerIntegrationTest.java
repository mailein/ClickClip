package com.example.FlashCards.controllers;

import com.example.FlashCards.DTOs.UserDTO;
import com.example.FlashCards.FlashCardsApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = FlashCardsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate testRestTemplate;

    private final String host = "http://localhost";

    private String baseURL;

    @BeforeEach
    void setUp() {
        baseURL = host + ":" + port + "/users";
    }

    @Test
    void getUsers() throws URISyntaxException {
        List response = this.testRestTemplate.getForObject(new URI(baseURL), List.class);
        assertEquals(1, response.size());//preloaded a User
    }

    @Test
    void postUser() throws URISyntaxException {
        String name = "dummy";
        String password = "1234";
        UserDTO userDTO = new UserDTO();
        userDTO.setName(name);
        userDTO.setPassword(password);

        ResponseEntity<UserDTO> responseEntity = this.testRestTemplate.postForEntity(new URI(baseURL), userDTO, UserDTO.class);

        assertEquals(HttpStatusCode.valueOf(201), responseEntity.getStatusCode());
//        assertEquals(name, responseObj.getName());
//        assertEquals(password, responseObj.getPassword());
//        assertNotNull(responseObj.getId());
    }

    @Test
    void registerUser() throws URISyntaxException {
        String name = "dummy";
        String password = "1234";

        URI uri = new URI(baseURL + "/register");
        UriComponentsBuilder builder = UriComponentsBuilder.fromUri(uri)
                .queryParam("name", name)
                .queryParam("password", password);
        URI finalURI = builder.build().encode().toUri();

        RequestEntity<Void> request = RequestEntity.post(finalURI).build();
        ResponseEntity<UserDTO> responseEntity = this.testRestTemplate.exchange(request, UserDTO.class);

//        ResponseEntity<UserDTO> responseEntity = this.testRestTemplate
//                .postForEntity(finalURI, new HttpEntity<>(new HttpHeaders()), UserDTO.class);

        assertEquals(HttpStatusCode.valueOf(201), responseEntity.getStatusCode());
//        assertEquals(name, responseObj.getName());
//        assertEquals(password, responseObj.getPassword());
//        assertNotNull(responseObj.getId());
    }
}
