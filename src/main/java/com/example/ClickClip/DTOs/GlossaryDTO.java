package com.example.ClickClip.DTOs;

import lombok.Data;

import java.time.Instant;

@Data
public class GlossaryDTO {
    private Long id;
    private String name;
    private UserDTO userDTO;
    private Instant createdAt;
    private Instant updatedAt;
}
