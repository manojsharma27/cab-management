package com.ms.cabmgmt.exceptions;

import org.springframework.http.HttpStatus;

public class CabNotFoundException extends CabServiceException {

    private static final String MSG = "Cab with id : %s already exists";

    public CabNotFoundException(String cabId) {
        super(String.format(MSG, cabId), HttpStatus.NOT_FOUND);
    }
}