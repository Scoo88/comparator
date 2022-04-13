package com.price.comparator.check.store.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class StoreControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = StoreException.class)
    public ResponseEntity<Object> exception(StoreException exception) {
        String className = String.valueOf(exception.getClass());
        ResponseBase responseBase = new ResponseBase(exception.getLocalizedMessage(), exception.getHttpStatus());
        return new ResponseEntity<>(responseBase, Messages.STORE_ALREADY_EXISTS.getHttpStatus());
    }
}
