package com.example.Net_teca.services;

import com.example.Net_teca.dto.UpdateBookRequest;
import com.example.Net_teca.entities.Author;
import com.example.Net_teca.entities.Book;
import com.example.Net_teca.entities.enums.BookStatus;
import com.example.Net_teca.exceptions.CustomException;
import com.example.Net_teca.repository.AuthorRepository;
import com.example.Net_teca.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @Transactional
    public Book addBook(
            String isbn,
            Integer publicationYear,
            Integer quantityAvailable,
            BookStatus status,
            String title,
            List<Author> authors
    ) throws CustomException {

        if (bookRepository.findByIsbn(isbn).isPresent()) {
            throw new CustomException("Book with ISBN " + isbn + " already exists!");
        }

        Book book = new Book();
        book.setIsbn(isbn);
        book.setPublicationYear(publicationYear);
        book.setQuantityAvailable(quantityAvailable);
        book.setStatus(status);
        book.setTitle(title);

        // Resolver autores (buscar ou criar)
        for (Author inputAuthor : authors) {
            Optional<Author> existing = authorRepository.findByNameAndNationality(
                    inputAuthor.getName(),
                    inputAuthor.getNationality()
            );

            Author author = existing.orElseGet(() -> {
                Author newAuthor = new Author();
                newAuthor.setName(inputAuthor.getName());
                newAuthor.setNationality(inputAuthor.getNationality());
                return authorRepository.save(newAuthor);
            });

            book.getAuthors().add(author);
        }

        return bookRepository.save(book);
    }

   @Transactional
   public Book updateBook(Long id, UpdateBookRequest request) throws CustomException {
       Book book = bookRepository.findById(id)
               .orElseThrow(() -> new CustomException("Book not found with id: " + id));

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

   @Transactional(readOnly = true)
   public List<Book> listAllBooks() {
 
       return bookRepository.findAll(); 
   }

   @Transactional(readOnly = true)
   public List<Book> searchByAuthorOrBookName(String query) {
       // Deve usar query com JOIN FETCH
       return bookRepository.searchByTitleOrAuthorName(query);
   }

   @Transactional
   public void deleteBook(Long id) throws CustomException {
        Optional<Book> book = bookRepository.findById(id);
        
        if (book.isEmpty()) {
            throw new CustomException("Book not found with id: " + id);
        }
        
       bookRepository.deleteById(id);
   }


}