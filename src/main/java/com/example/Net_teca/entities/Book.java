package com.example.Net_teca.entities;

import com.example.Net_teca.entities.enums.BookStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String isbn;

    @Column(nullable = false)
    private Integer publicationYear;

    @Column(nullable = false)
    private Integer quantityAvailable;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookStatus status;


    @ManyToMany
    @JoinTable(name = "book_author", joinColumns = @JoinColumn(name = "book_id"), inverseJoinColumns = @JoinColumn(name = "author_id"))
    @JsonIgnore
    private Set<Author> authors = new HashSet<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JsonIgnore
    private List<Loan> loans = new ArrayList<>();
    
    
    public boolean isAvailable() {
        return quantityAvailable > 0;
    }

    public void reduceQuantity() {
        if (quantityAvailable <= 0) {
            throw new IllegalStateException("Livro indisponível para empréstimo");
        }
        quantityAvailable--;
        refreshStatusIfNeeded();
    }

    public void increaseQuantity() {
        quantityAvailable++;
        refreshStatusIfNeeded();
    }

    private void refreshStatusIfNeeded() {
        if (quantityAvailable > 0)
            status = BookStatus.DISPONIVEL;
        else
            status = BookStatus.EMPRESTADO;
    }

    public void registerLoan() {
        reduceQuantity();
    }
}
