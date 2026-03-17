package com.example.Net_teca.controller;

import com.example.Net_teca.dto.*;
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

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
@Tag(name = "Book")
public class BookController {

    private final BookService bookService;

    @PostMapping("/add")
    @Operation(summary = "Add new book")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Book created",
                    content = @Content(schema = @Schema(implementation = BookAddResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data / ISBN already exists")
    })
    public ResponseEntity<BookAddResponse> addBook(@RequestBody BookAuthorRequest request) throws CustomException {
        Book book = request.getBook();
        List<Author> authors = request.getAuthors();

        Book addedBook = bookService.addBook(book.getIsbn(), book.getPublicationYear(), book.getQuantityAvailable(), book.getStatus(), book.getTitle(), authors);

        BookAddResponse response = new BookAddResponse();
        response.setBook(addedBook);
        response.setAuthorsNames(addedBook.getAuthors().stream().map(Author::getName).toList());

        return ResponseEntity.ok(response);
    }


   @PutMapping("/update/{id}")
   @Operation(summary = "Update existing book")
   public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody UpdateBookRequest request) throws CustomException {
       return ResponseEntity.ok(bookService.updateBook(id, request));
   }


   @GetMapping("/list/all")
   @Operation(summary = "List all books")
   public ResponseEntity<List<BookListResponse>> listAllBooks() {
       List<Book> books = bookService.listAllBooks();

       if (books.isEmpty()) {
           return ResponseEntity.noContent().build();
       }

       List<BookListResponse> dtos = books.stream()
               .map(this::toBookListResponse)
               .toList();

       return ResponseEntity.ok(dtos);
   }

   @GetMapping("/search")
   @Operation(summary = "Search books by title or author name")
   public ResponseEntity<List<BookListResponse>> search(@RequestParam("q") String query) {
       List<Book> books = bookService.searchByAuthorOrBookName(query);

       List<BookListResponse> dtos = books.stream()
               .map(this::toBookListResponse)
               .toList();

       return ResponseEntity.ok(dtos);
   }

   @DeleteMapping("/delete/{id}")
   @Operation(summary = "Delete book by id")
   public ResponseEntity<MessageResponse> deleteBook(@PathVariable Long id) throws CustomException {
       bookService.deleteBook(id);
       return ResponseEntity.status(HttpStatus.OK) 
       .body(new MessageResponse("Book deleted successfully!"));
   }

   @GetMapping("/isAvailable")
   @Operation(summary = "Check if book is available")
   public ResponseEntity<AvailabilityCheckResponse> isAvailable(@RequestParam("isbn") String isbn) throws CustomException{

    try {
        Book book = bookService.isAvailable(isbn);
        return ResponseEntity.ok(new AvailabilityCheckResponse(true, "Book is available", toBookListResponse(book)));
    } catch (CustomException e) {
        return ResponseEntity.ok(new AvailabilityCheckResponse(false, "Book is not available", null));
    }

   }


// Helpers de mapeamento (podem ir para um BookMapper no futuro)
private BookListResponse toBookListResponse(Book book) {
    return new BookListResponse(
            book.getTitle(),
            book.getPublicationYear(),
            book.getQuantityAvailable(),
            book.getStatus(),
            book.getIsbn(),
            book.getAuthors().stream()
                    .toList()
    );
}

}