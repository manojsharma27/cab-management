package com.ms.cabmgmt.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CabServiceException extends RuntimeException {

    private HttpStatus httpStatus;

    public CabServiceException() {
    }

    public CabServiceException(String message) {
        super(message);
        httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public CabServiceException(String message, Throwable cause) {
        super(message, cause);
        httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public CabServiceException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public CabServiceException(String message, Throwable cause, HttpStatus httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }
}
