package com.kwon.myshop.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public abstract class MyshopException extends RuntimeException {

    private Map<String, List<String>> validation = new HashMap<>();

    public MyshopException(String message) {
        super(message);
    }

    public abstract HttpStatus getStatusCode();

}
