package com.example.Net_teca.controller;

import com.example.Net_teca.dto.LoanRequest;
import com.example.Net_teca.dto.LoanResponse;
import com.example.Net_teca.dto.MessageResponse;
import com.example.Net_teca.entities.Loan;
import com.example.Net_teca.exceptions.CustomException;
import com.example.Net_teca.services.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/loan")
@Tag(name = "Loan")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("/add")
    @Operation(summary = "Create a new loan")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Loan Created",
            content = @Content(schema = @Schema(implementation = LoanResponse.class))
        ),
        @ApiResponse(responseCode = "400", description = "Invalid Loan, book already lended")
    })
    public ResponseEntity<LoanResponse> lendBook(@RequestBody LoanRequest request) throws CustomException {
        Loan loan = loanService.lendBook(request);
        LoanResponse response = new LoanResponse(loan);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/my-loans")
    @Operation(summary = "Get all loans for the authenticated user")
    @ApiResponse(responseCode = "200", description = "List of user's loans")
    public ResponseEntity<List<LoanResponse>> getMyLoans(Authentication authentication) throws CustomException {
        String username = authentication.getName(); 
        List<Loan> loans = loanService.getLoansForUser(username);

        if (loans.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<LoanResponse> response = loans.stream().map(LoanResponse::new).toList();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/return/{loanId}")
    @Operation(summary = "Return a book")
    @ApiResponse(responseCode = "200", description = "Book returned successfully")
    public ResponseEntity<MessageResponse> returnBook(Authentication authentication, @PathVariable Long loanId) throws CustomException {
        String username = authentication.getName(); 
        
        loanService.returnBook(loanId, username);
        return ResponseEntity.ok(new MessageResponse("Book returned successfully"));
    }

}
