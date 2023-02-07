package com.example.ClickClip.controllers;

import com.example.ClickClip.DTOs.UserDTO;
import com.example.ClickClip.entities.User;
import com.example.ClickClip.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    @Autowired
    UserService userService;


    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public UserDTO getUser(@PathVariable("userId") Long userId) {
        return userService.getUserById(userId);
    }

    @PostMapping
    public UserDTO addUser(@RequestBody User user) {
        return userService.add(user);
    }

    @PutMapping("/{id}")
    public UserDTO updateUser(@RequestBody User user, @PathVariable("id") Long id) {
        return userService.updateUser(user, id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        userService.deleteById(id);
    }
}
