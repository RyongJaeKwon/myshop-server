package com.kwon.myshop.exception;

import org.springframework.http.HttpStatus;

public class OrderCancelException extends MyshopException {

    public OrderCancelException(String message) {
        super(message);
    }
    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }
}
