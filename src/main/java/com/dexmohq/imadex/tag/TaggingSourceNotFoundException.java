package com.dexmohq.imadex.tag;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TaggingSourceNotFoundException extends Exception {

    public TaggingSourceNotFoundException(String source) {
        super("No service for source identifier '" + source + "' found");
    }
}
