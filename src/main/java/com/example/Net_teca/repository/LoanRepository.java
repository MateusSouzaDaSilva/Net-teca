package com.example.Net_teca.repository;

import com.example.Net_teca.entities.Loan;

import java.util.List;

import com.example.Net_teca.entities.enums.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository  extends JpaRepository<Loan, Long> {
    List<Loan> findByUserId(Long userId);

    boolean existsByBookIdAndUserIdAndLoanStatus(Long bookId, Long userId, LoanStatus loanStatus);
}
