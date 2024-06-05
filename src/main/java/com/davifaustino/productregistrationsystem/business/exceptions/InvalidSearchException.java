package com.davifaustino.productregistrationsystem.business.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidSearchException extends RuntimeException {

    public InvalidSearchException(String message) {
        super(message);
    }
}
