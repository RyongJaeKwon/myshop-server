package com.kwon.myshop.exception;

import org.springframework.http.HttpStatus;

public class ItemNotFoundException extends MyshopException {

    private static final String MESSAGE = "존재하지 않는 상품입니다.";

    public ItemNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.NOT_FOUND;
    }
}
