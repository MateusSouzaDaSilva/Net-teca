package com.example.Net_teca.exceptions;

import lombok.*;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExceptionResponse {

    private Date date;
    private String message;
    private int code;
}
