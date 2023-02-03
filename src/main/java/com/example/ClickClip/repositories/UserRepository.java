package com.example.ClickClip.repositories;

import com.example.ClickClip.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // TODO: check if return List<User> or Optional
    public Optional<List<User>> findByUsername(String username);
}
