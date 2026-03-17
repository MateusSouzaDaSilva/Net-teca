package com.example.Net_teca.dto;

public record AvailabilityCheckResponse(
     boolean available,
     String message,
     BookListResponse book
) {
    
}
