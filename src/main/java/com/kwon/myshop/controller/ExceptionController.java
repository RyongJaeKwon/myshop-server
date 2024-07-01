package com.kwon.myshop.controller;

import com.kwon.myshop.dto.ErrorResponse;
import com.kwon.myshop.exception.MyshopException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException", e);

        List<FieldError> fieldErrors = e.getFieldErrors();

        Map<String, List<String>> validation = fieldErrors.stream().collect(Collectors.toMap(
                FieldError::getField,
                fieldError -> {
                    List<String> validations = new ArrayList<>();
                    validations.add(fieldError.getDefaultMessage());
                    return validations;
                },
                (list1, list2) -> {
                    list1.add(list2.get(0));
                    return list1;
                }
        ));

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.toString())
                .message("잘못된 요청입니다.")
                .validation(validation)
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler(MyshopException.class)
    public ResponseEntity<ErrorResponse> myshopException(MyshopException e) {
        log.error("MyshopException: " , e);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(e.getStatusCode().toString())
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();

        return ResponseEntity
                .status(e.getStatusCode())
                .body(errorResponse);
    }



}
