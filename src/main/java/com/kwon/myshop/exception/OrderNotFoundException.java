package com.kwon.myshop.exception;

import org.springframework.http.HttpStatus;

public class OrderNotFoundException extends MyshopException{

    private static final String MESSAGE = "존재하지 않는 주문입니다.";

    public OrderNotFoundException() {
        super(MESSAGE);
    }
    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.NOT_FOUND;
    }
}
