package com.example.Net_teca.dto;

import java.util.ArrayList;
import java.util.List;

import com.example.Net_teca.entities.Book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookAddResponse {
    private Book book;
    private List<String> authorsNames = new ArrayList<>();
}
