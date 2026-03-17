package com.example.Net_teca.dto;

import com.example.Net_teca.entities.Loan;
import com.example.Net_teca.entities.enums.LoanStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LoanResponse(
        Long id,
        LocalDate loanDate,
        LocalDate dueDate,
        LocalDate returnDate,
        BigDecimal overdueFine,
        LoanStatus loanStatus,
        UserResponse user,
        BookResponse book
) {
    public LoanResponse(Loan loan) {
        this(
                loan.getId(),
                loan.getLoanDate(),
                loan.getDueDate(),
                loan.getReturnDate(),
                loan.getOverdueFine(),
                loan.getLoanStatus(),
                new UserResponse(loan.getUser()),
                new BookResponse(loan.getBook())
        );
    }
}
