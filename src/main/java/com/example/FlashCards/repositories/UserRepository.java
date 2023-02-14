package com.example.FlashCards.repositories;

import com.example.FlashCards.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // TODO: check if return List<User> or Optional
    public Optional<User> findByName(String name);
}
