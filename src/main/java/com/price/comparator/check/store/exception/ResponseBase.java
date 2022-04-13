package com.price.comparator.check.store.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class ResponseBase {
    private String message;
    private LocalDateTime timestamp = LocalDateTime.now();
    private HttpStatus httpStatus;

    public ResponseBase(String message, HttpStatus httpStatus ) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
