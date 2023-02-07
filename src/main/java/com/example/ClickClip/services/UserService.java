package com.example.ClickClip.services;

import com.example.ClickClip.DTOs.UserDTO;
import com.example.ClickClip.entities.User;
import com.example.ClickClip.exceptions.InvalidRequestException;
import com.example.ClickClip.exceptions.NotFoundException;
import com.example.ClickClip.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

//    @Autowired
    ModelMapper modelMapper = new ModelMapper();

    public UserDTO add(User user) {
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

        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDTO.class);
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

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id " + id + " is not found."));
        return modelMapper.map(user, UserDTO.class);
    }

    public UserDTO getUserByName(String name) {
        User user = userRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("User with name " + name + " is not found."));
        return modelMapper.map(user, UserDTO.class);
    }

    public List<UserDTO> getAllUsers() {
        List<User> allUsers = userRepository.findAll();
        return allUsers.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    public UserDTO updateUser(User user, Long userId) {
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
        User updatedUser = userRepository.save(existingUser);
        return modelMapper.map(updatedUser, UserDTO.class);
    }
}
