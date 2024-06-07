package com.kenjy.bookapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicatePurchaseRequestForUserException extends RuntimeException {
    public DuplicatePurchaseRequestForUserException(String message) {
        super(message);
    }
}
