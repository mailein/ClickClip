package com.example.ClickClip.DTOs;

import com.example.ClickClip.entities.Glossary;
import lombok.Data;

@Data
public class WordDTO {
    private Long id;
    private String name;
    private Glossary glossary;
}
