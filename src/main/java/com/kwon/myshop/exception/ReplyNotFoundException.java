package com.kwon.myshop.exception;

import org.springframework.http.HttpStatus;

public class ReplyNotFoundException extends MyshopException{

    private static final String MESSAGE = "존재하지 않는 댓글입니다.";

    public ReplyNotFoundException() {
        super(MESSAGE);
    }
    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.NOT_FOUND;
    }
}
