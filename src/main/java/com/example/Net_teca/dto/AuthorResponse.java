package com.example.Net_teca.dto;

import com.example.Net_teca.entities.Author;

public record AuthorResponse(
    Long id,
    String name,
    String nationality
) {
    public AuthorResponse(Author author) {
        this(author.getId(), author.getName(), author.getNationality());
    }
}
