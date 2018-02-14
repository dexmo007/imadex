package com.dexmohq.imadex.tag;

public class TaggingProcessingException extends Exception {

    public TaggingProcessingException() {
    }

    public TaggingProcessingException(String message) {
        super(message);
    }

    public TaggingProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaggingProcessingException(Throwable cause) {
        super(cause);
    }

    public TaggingProcessingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
