package com.price.comparator.check.store.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum Messages {

    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, 1000, "Store not found."),
    STORE_ALREADY_EXISTS(HttpStatus.CONFLICT, 1001, "Store already exists."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, 1002, "Category does not exist.");

    HttpStatus httpStatus;
    long code;
    String message;

    Messages(HttpStatus httpStatus, long code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
