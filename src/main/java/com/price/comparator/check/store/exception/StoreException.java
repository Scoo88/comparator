package com.price.comparator.check.store.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = true)
public class StoreException extends Exception{
    private final Messages messages;
    private final Throwable throwable;
    private HttpStatus httpStatus;

    public StoreException(Messages messages) {
        super(messages.getMessage());
        this.messages = messages;
        this.throwable = new Throwable(messages.getMessage());
        this.httpStatus = messages.getHttpStatus();
    }

    public StoreException(Messages messages, Throwable throwable) {
        super(messages.getMessage(), throwable);
        this.messages = messages;
        this.throwable = throwable;
    }

    public StoreException(Messages messages, String exception) {
        super(messages.getMessage());
        this.messages = messages;
        this.throwable = new Throwable(exception);
    }
}
