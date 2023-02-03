package com.example.ClickClip.services;

import com.example.ClickClip.entities.User;
import com.example.ClickClip.exceptions.InvalidRequestException;
import com.example.ClickClip.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User addUser(User user) {
        // check user
        if (user.getId() == null) {
            throw new InvalidRequestException("User id is null.");
        }

        // check repo
        if (userRepository.findById(user.getId()).isPresent()) {
            throw new InvalidRequestException("User with id " + user.getId() + " already exists.");
        }

        if (userRepository.findByUsername(user.getName()).isPresent()) {
            throw new InvalidRequestException("User with username " + user.getName() + " already exists.");
        }

        // add user
        return userRepository.save(user);
    }

    public void removeUser() {

    }


}
