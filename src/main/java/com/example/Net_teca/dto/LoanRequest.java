package com.example.Net_teca.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoanRequest(
        @JsonProperty("bookId") Long bookId,
        @JsonProperty("userId") Long userId
) {

}
