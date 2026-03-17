package com.example.Net_teca.dto;

import com.example.Net_teca.entities.Book;
import com.example.Net_teca.entities.enums.BookStatus;
import java.util.Set;
import java.util.stream.Collectors;

public record BookResponse(
    Long id,
    String title,
    String isbn,
    Integer publicationYear,
    Integer quantityAvailable,
    BookStatus status,
    Set<AuthorResponse> authors
) {
    public BookResponse(Book book) {
        this(
                book.getId(),
                book.getTitle(),
                book.getIsbn(),
                book.getPublicationYear(),
                book.getQuantityAvailable(),
                book.getStatus(),
                book.getAuthors().stream().map(AuthorResponse::new).collect(Collectors.toSet())
        );
    }
}
