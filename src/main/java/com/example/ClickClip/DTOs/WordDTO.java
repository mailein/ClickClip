package com.example.ClickClip.DTOs;

import lombok.Data;

import java.time.Instant;

@Data
public class WordDTO {
    private Long id;
    private String name;
    private GlossaryDTO glossaryDTO;
    private Instant createdAt;
    private Instant updatedAt;
}
