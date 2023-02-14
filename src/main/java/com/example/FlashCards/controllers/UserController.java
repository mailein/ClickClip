package com.example.FlashCards.controllers;

import com.example.FlashCards.DTOs.UserDTO;
import com.example.FlashCards.services.UserService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUser(@PathVariable("userId") Long userId) {
        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDTO> addUser(@RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(userService.add(userDTO), HttpStatus.CREATED);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestParam("username") @NotNull String name,
                                            @RequestParam("password") @NotNull String password) {
        UserDTO userDTO = new UserDTO();
        userDTO.setName(name);
        userDTO.setPassword(password);
        return this.addUser(userDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam("username") @NotNull String name,
                                        @RequestParam("password") @NotNull String password) {
        UserDTO userDTO = new UserDTO();
        userDTO.setName(name);
        userDTO.setPassword(password);
        boolean success = userService.login(userDTO);
        if (success) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("Hello, " + name);
        } else {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Incorrect username or password");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO, @PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.updateUser(userDTO, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return new ResponseEntity<>("Deleted use with id " + id, HttpStatus.OK);
    }
}
