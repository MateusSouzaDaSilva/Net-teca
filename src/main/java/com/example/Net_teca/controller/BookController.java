package com.example.Net_teca.controller;

import com.example.Net_teca.dto.BookAddResponse;
import com.example.Net_teca.dto.BookAuthorRequest;
import com.example.Net_teca.dto.UpdateBookRequest;
import com.example.Net_teca.entities.Author;
import com.example.Net_teca.entities.Book;
import com.example.Net_teca.exceptions.CustomException;
import com.example.Net_teca.services.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
@Tag(name = "Book")
public class BookController {

    private final BookService bookService;

    @Operation(
            summary = "Add new book"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Book added",
                            content = {@Content(schema = @Schema(implementation = Book.class))}
                    ),
            }
    )
    @PostMapping("/add")
    public ResponseEntity<BookAddResponse> addBook(@RequestBody BookAuthorRequest request) throws CustomException {
        Book book = request.getBook();
        List<Author> authors = request.getAuthors();

        Book addedBook = bookService.addBook(book.getIsbn(), book.getPublicationYear(), book.getQuantityAvailable(), book.getStatus(),  book.getTitle(), authors);

        BookAddResponse response = new BookAddResponse();
        response.setBook(addedBook);
        response.setAuthorsNames(addedBook.getAuthors().stream().map(Author::getName).toList());
        
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Update book"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Book updated",
                            content = {@Content(schema = @Schema(implementation = Book.class))}),
                    }
                )
    @PutMapping("/update/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody UpdateBookRequest request) throws CustomException {
        return ResponseEntity.ok(bookService.updateBook(id, request));
    }
}


