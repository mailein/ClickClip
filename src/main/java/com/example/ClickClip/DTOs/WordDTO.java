package com.example.ClickClip.DTOs;

import com.example.ClickClip.entities.Glossary;
import lombok.Data;

import java.time.Instant;

@Data
public class WordDTO {
    private Long id;
    private String name;
    private Glossary glossary;
    private Instant createdAt;
    private Instant updatedAt;
}
