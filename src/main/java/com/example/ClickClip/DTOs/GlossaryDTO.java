package com.example.ClickClip.DTOs;

import com.example.ClickClip.entities.User;
import lombok.Data;

@Data
public class GlossaryDTO {
    private Long id;
    private String name;
    private User user;
}
