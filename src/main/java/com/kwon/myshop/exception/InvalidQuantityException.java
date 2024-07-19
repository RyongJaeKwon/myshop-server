package com.kwon.myshop.exception;

import org.springframework.http.HttpStatus;

public class InvalidQuantityException extends MyshopException {

    private static final String MESSAGE = "더 이상 수량을 줄일 수 없습니다";

    public InvalidQuantityException() {
        super(MESSAGE);
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }
}
