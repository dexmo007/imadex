package com.dexmohq.imadex.storage;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidPaginationException extends RuntimeException {
    InvalidPaginationException(String message) {
        super(message);
    }
}
