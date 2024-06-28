package com.kwon.myshop.exception;

import org.springframework.http.HttpStatus;

public class MemberNotFoundException extends MyshopException {

    private static final String MESSAGE = "존재하지 않는 회원입니다.";

    public MemberNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.NOT_FOUND;
    }
}
