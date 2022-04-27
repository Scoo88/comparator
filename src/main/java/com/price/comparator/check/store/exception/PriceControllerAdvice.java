package com.price.comparator.check.store.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class PriceControllerAdvice extends ResponseEntityExceptionHandler {

    //TODO
    // - finish this
    @ExceptionHandler(value = PriceException.class)
    public ResponseEntity<Object> exception(PriceException exception) {
        String className = String.valueOf(exception.getClass());
        ResponseBase responseBase = new ResponseBase(exception.getLocalizedMessage(), exception.getHttpStatus());
        return new ResponseEntity<>(responseBase, Messages.STORE_ALREADY_EXISTS.getHttpStatus());
    }
}
