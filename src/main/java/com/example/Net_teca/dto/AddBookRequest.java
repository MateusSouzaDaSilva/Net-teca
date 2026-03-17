package com.example.Net_teca.dto;


import com.example.Net_teca.entities.Author;
import com.example.Net_teca.entities.enums.BookStatus;

import java.util.List;


public record AddBookRequest(
        String isbn,
        Integer publicationYear,
        Integer quantityAvailable,
        BookStatus status,
        String title,
        List<Author> authors
) {
}
