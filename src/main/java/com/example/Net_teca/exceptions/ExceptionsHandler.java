package com.example.Net_teca.exceptions;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionsHandler extends ResponseEntityExceptionHandler {


    private final MessageSource messageSource;

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ExceptionResponse> customExceptionHandler(CustomException ex){
        ExceptionResponse response = ExceptionResponse
                .builder()
                .message(ex.getMessage())
                .code(HttpStatus.BAD_REQUEST.value())
                .date(new Date())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException exception,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request
    ){
        var validationError = new ValidationError();
        List<FieldValidationError> fieldErrorList = this.mapBindExceptionToFieldError(exception);

        validationError.setMessage("One or more invalid fields");
        validationError.setDate(new Date());
        validationError.setCode(HttpStatus.BAD_REQUEST.value());
        validationError.setErrors(fieldErrorList);

        return this.handleExceptionInternal(exception, validationError, headers, HttpStatus.BAD_REQUEST, request);
    }

    private List<FieldValidationError> mapBindExceptionToFieldError(BindException exception) {
        List<FieldValidationError> fieldErrorsList = new ArrayList<>();

        for(ObjectError error : exception.getBindingResult().getAllErrors()) {
            var fieldName = ((FieldError) error).getField();
            var errorDescription = this.messageSource.getMessage(error, LocaleContextHolder.getLocale());

            var fieldValidationError = new FieldValidationError();

            fieldValidationError.setNameField(fieldName);
            fieldValidationError.setErrorMessage(errorDescription);

            fieldErrorsList.add(fieldValidationError);
        }
        return fieldErrorsList;
    }
}
