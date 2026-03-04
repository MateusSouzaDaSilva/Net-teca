package com.example.Net_teca.dto;

import java.util.List;

import com.example.Net_teca.entities.Author;
import com.example.Net_teca.entities.Book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookAuthorRequest {
    private Book book;
    private List<Author> authors;
}
