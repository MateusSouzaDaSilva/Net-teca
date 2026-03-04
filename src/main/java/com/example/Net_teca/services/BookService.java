package com.example.Net_teca.services;

import com.example.Net_teca.entities.Author;
import com.example.Net_teca.entities.Book;
import com.example.Net_teca.entities.enums.BookStatus;
import com.example.Net_teca.exceptions.CustomException;
import com.example.Net_teca.repository.AuthorRepository;
import com.example.Net_teca.repository.BookRepository;
import com.example.Net_teca.dto.UpdateBookRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class BookService {

    @Autowired
    private final BookRepository bookRepository;

    @Autowired
    private final AuthorRepository authorRepository;

    @Transactional
    public Book addBook(String isbn, Integer publicationYear, Integer quantityAvailable, BookStatus status, String title,
                        List<Author> authors) throws CustomException {
        if (this.bookRepository.findByIsbn(isbn).isPresent()) {
            throw new CustomException("Book already exists!");
        }

        Book book = new Book();
        book.setIsbn(isbn);
        book.setPublicationYear(publicationYear);
        book.setQuantityAvailable(quantityAvailable);
        book.setStatus(status);
        book.setTitle(title);
        book = bookRepository.save(book);

        for (Author author : authors) {
            Optional<Author> searchedAuthor = this.authorRepository.findByNameAndNationality(author.getName(), author.getNationality());
        
            Author foundAuthor = searchedAuthor.orElseGet(() -> {
                Author newAuthor = new Author();
                newAuthor.setName(author.getName());
                newAuthor.setNationality(author.getNationality());
                newAuthor = authorRepository.save(newAuthor);
                return newAuthor;
            });

            book.getAuthors().add(foundAuthor);
        }

        return bookRepository.save(book);
    }

    @Transactional
    public Book updateBook(Long id, UpdateBookRequest request) throws CustomException {
        Book book = bookRepository.findById(id).orElseThrow(() -> new CustomException("Book not found!"));
        if (request.getTitle() != null) {
            book.setTitle(request.getTitle());
        }
        if (request.getPublicationYear() != null) {
            book.setPublicationYear(request.getPublicationYear());
        }
        if (request.getQuantityAvailable() != null) {
            book.setQuantityAvailable(request.getQuantityAvailable());
        }
        if (request.getStatus() != null) {
            book.setStatus(request.getStatus());
        }
    
        return bookRepository.save(book);
    }
}
