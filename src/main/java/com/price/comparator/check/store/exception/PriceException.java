package com.price.comparator.check.store.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = true)
public class PriceException extends Exception{
    private final Messages messages;
    private final Throwable throwable;
    private HttpStatus httpStatus;

    private long code;

    public PriceException(Messages messages) {
        super(messages.getMessage());
        this.messages = messages;
        this.throwable = new Throwable(messages.getMessage());
        this.httpStatus = messages.getHttpStatus();
    }

    public PriceException(Messages messages, Throwable throwable) {
        super(messages.getMessage(), throwable);
        this.messages = messages;
        this.throwable = throwable;
    }

    public PriceException(Messages messages, String exception) {
        super(messages.getMessage());
        this.messages = messages;
        this.throwable = new Throwable(exception);
    }
}
