package com.example.ClickClip.DTOs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.Instant;

@Data
public class UserDTO {

    private Long id;
    private String name;
    private String password;
    private Instant createdAt;
    private Instant updatedAt;

    @JsonIgnore
    public String getPassword() {
        return password;
    }
}
