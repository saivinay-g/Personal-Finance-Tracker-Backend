package com.personalfinancetracker.personalfinancetracker.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorDetails {
    private LocalDateTime timestamp;   // standard name
    private int status;                // HTTP status code
    private String error;              // HTTP reason phrase
    private String message;            // actual error message
    private String path;               // request path
}
