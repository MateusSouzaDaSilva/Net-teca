package com.example.Net_teca.services;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Net_teca.dto.LoanRequest;
import com.example.Net_teca.entities.Book;
import com.example.Net_teca.entities.Loan;
import com.example.Net_teca.entities.User;
import com.example.Net_teca.entities.enums.LoanStatus;
import com.example.Net_teca.exceptions.CustomException;
import com.example.Net_teca.repository.BookRepository;
import com.example.Net_teca.repository.LoanRepository;
import com.example.Net_teca.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    
    @Transactional
    public Loan lendBook(LoanRequest request) throws CustomException {
        
        if (request.bookId() == null) {
            throw new CustomException("Book ID is required");
        }
        
        if (request.userId() == null) {
            throw new CustomException("User ID is required");
        }
        
        Book book = bookRepository.findById(request.bookId()).orElseThrow(() -> new CustomException("Book not found"));
        
        User user = userRepository.findById(request.userId()).orElseThrow(() -> new CustomException("User not found"));

        boolean hasActiveLoan = loanRepository.existsByBookIdAndUserIdAndLoanStatus(request.bookId(), request.userId(), LoanStatus.ATIVO);

        if (hasActiveLoan) {
            throw new CustomException("O usuário já possui um empréstimo ativo para este livro.");
        }

        if (!book.isAvailable()) {
            throw new CustomException("Book out of stock!");       
        }

       

        book.registerLoan();
        bookRepository.save(book);

        Loan newLoan = new Loan();
        newLoan.setBook(book);
        newLoan.setUser(user);
        newLoan.setLoanDate(LocalDate.now());
        newLoan.setDueDate(calculateDueDate(LocalDate.now()));
        newLoan.setLoanStatus(LoanStatus.ATIVO);
        newLoan.setOverdueFine(BigDecimal.ZERO);
        
        return loanRepository.save(newLoan);
    }

    @Transactional(readOnly = true)
    public List<Loan> getUserLoans(Long userId) throws CustomException {
        if (!userRepository.existsById(userId)) {
            throw new CustomException("User not found");
        }

        return loanRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Loan> getLoansForUser(String username) throws CustomException {
        // Assuming username from JWT is the user's email
        User user = userRepository.findByName(username)
                .orElseThrow(() -> new CustomException("Authenticated user not found in database with email: " + username));

        return loanRepository.findByUserId(user.getId());
    }

    @Transactional
    public void returnBook(Long loanId, String username) throws CustomException {    
        Loan loan = loanRepository.findById(loanId).orElseThrow(() -> new CustomException("Loan not found"));

        if (!loan.getUser().getName().equals(username)) {
            throw new CustomException("Este empréstimo não pertence ao usuário autenticado.");
        }

        if (loan.getLoanStatus() == LoanStatus.DEVOLVIDO) {
            throw new CustomException("Este empréstimo já foi devolvido.");
        }

        BigDecimal potentialFine = calculateOverdueFine(loan.getDueDate(), LocalDate.now());
        if (potentialFine.compareTo(BigDecimal.ZERO) > 0) {
            throw new CustomException("Devolução em atraso! Multa pendente de: " + potentialFine);
        }

        loan.setReturnDate(LocalDate.now());
        loan.setLoanStatus(LoanStatus.DEVOLVIDO);
        loan.setOverdueFine(potentialFine);


        Book book = loan.getBook();
        book.increaseQuantity();
        bookRepository.save(book);

        loanRepository.save(loan);                      

    }

    private LocalDate calculateDueDate(LocalDate startDate) {
        LocalDate dueDate = startDate.plusDays(14);

        if(dueDate.getDayOfWeek() == DayOfWeek.SATURDAY) {
            return dueDate.plusDays(2);
        }

        if(dueDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
            return dueDate.plusDays(1);
        }

        return dueDate;
    }

    private BigDecimal calculateOverdueFine(LocalDate dueDate, LocalDate returnDate) {
        if (returnDate.isAfter(dueDate)) {
            long daysOverdue = returnDate.toEpochDay() - dueDate.toEpochDay();
            return BigDecimal.valueOf(daysOverdue * 2.0);
        }
        return BigDecimal.ZERO;
    }
}
