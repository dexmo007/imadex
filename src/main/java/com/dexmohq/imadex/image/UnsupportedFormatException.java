package com.dexmohq.imadex.image;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Arrays;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnsupportedFormatException extends Exception {

    public UnsupportedFormatException() {
        super("Unsupported image format. Use one of: "
                + Arrays.toString(ImageFormatValidator.ValidImageFormat.values()));
    }

}
