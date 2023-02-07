package com.example.ClickClip.services;

import com.example.ClickClip.entities.User;
import com.example.ClickClip.exceptions.InvalidRequestException;
import com.example.ClickClip.exceptions.NotFoundException;
import com.example.ClickClip.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    ModelMapper modelMapper;

    public User add(User user) {
        // check user
        if (user.getId() == null) {
            throw new InvalidRequestException("User id is null.");
        }

        // check repo
        if (userRepository.findById(user.getId()).isPresent()) {
            throw new InvalidRequestException("User with id " + user.getId() + " already exists.");
        }

        if (userRepository.findByName(user.getName()).isPresent()) {
            throw new InvalidRequestException("User with username " + user.getName() + " already exists.");
        }

        return userRepository.save(user);
    }

    public void deleteById(Long id) {
        // check user
        if (id == null) {
            throw new InvalidRequestException("User id is null.");
        }

        // check repo
        if (userRepository.findById(id).isEmpty()) {
            throw new InvalidRequestException("User with id " + id + " doesn't exist.");
        }

        userRepository.deleteById(id);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id " + id + " is not found."));
    }

    public User getUserByName(String name) {
        return userRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("User with name " + name + " is not found."));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(User user, Long userId) {
        // check user
        if (userId == null) {
            throw new InvalidRequestException("User id is null.");
        }

        // check repo
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new InvalidRequestException("User with id " + userId + " doesn't exist.");
        }

        User existingUser = optionalUser.get();
        existingUser.setName(user.getName());
        existingUser.setPassword(user.getPassword());
        existingUser.setGlossaries(user.getGlossaries());
        return userRepository.save(existingUser);
    }
}
