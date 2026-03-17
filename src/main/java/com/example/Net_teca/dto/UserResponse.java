package com.example.Net_teca.dto;

import com.example.Net_teca.entities.User;

public record UserResponse(
    Long id,
    String name,
    String email
) {
    public UserResponse(User user) {
        this(user.getId(), user.getName(), user.getEmail());
    }
}
