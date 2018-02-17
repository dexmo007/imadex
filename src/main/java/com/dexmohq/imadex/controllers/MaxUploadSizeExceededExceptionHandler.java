package com.dexmohq.imadex.controllers;

import org.apache.tomcat.util.http.fileupload.FileUploadBase;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class MaxUploadSizeExceededExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({MultipartException.class, FileUploadBase.FileSizeLimitExceededException.class, java.lang.IllegalStateException.class})
    public ResponseEntity<?> handleSizeExceededException(final WebRequest request, final MultipartException ex) {
        return handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatus.PAYLOAD_TOO_LARGE, request);
    }

}
