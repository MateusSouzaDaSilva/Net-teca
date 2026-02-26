package com.example.Net_teca.repository;

import com.example.Net_teca.entities.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository  extends JpaRepository<Loan, Long> {
}
