package com.example.ClickClip.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.Instant;

@Data
public class UserDTO {

    private Long id;
    private String name;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private Instant createdAt;
    private Instant updatedAt;

//    @JsonIgnore
//    public String getPassword() {
//        return password;
//    }
//
//    @JsonProperty //otherwise password is NULL in database
//    public void setPassword(String password) {
//        this.password = password;
//    }
}
