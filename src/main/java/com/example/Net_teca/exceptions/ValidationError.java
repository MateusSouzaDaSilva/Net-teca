package com.example.Net_teca.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ValidationError extends ExceptionResponse {
    private List<FieldValidationError> errors;
}