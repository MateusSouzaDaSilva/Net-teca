package com.example.Net_teca.dto;

import java.util.List;

import com.example.Net_teca.entities.Author;
import com.example.Net_teca.entities.enums.BookStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookListResponse {
    private String title;
    private Integer publicationYear;
    private Integer quantityAvailable;
    private BookStatus status;
    private String isbn;
    private List<Author> authors;
}
