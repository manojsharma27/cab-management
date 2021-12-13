package com.ms.cabmgmt.exceptions;

import org.springframework.http.HttpStatus;

public class CabAlreadyExistsException extends CabServiceException {

    private static final String MSG = "Cab with id : %s already exists";

    public CabAlreadyExistsException(String cabId) {
        super(String.format(MSG, cabId), HttpStatus.BAD_REQUEST);
    }
}