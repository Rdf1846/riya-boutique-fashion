package com.riyaboutique.auth.exception;

import org.springframework.http.ResponseEntity;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }

}
