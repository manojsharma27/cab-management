package com.ms.cabmgmt.exceptions;

import org.springframework.http.HttpStatus;

public class CityNotSupportedException extends CabServiceException {

    private static final String MSG = "%s city is not onboarded yet.";

    public CityNotSupportedException(String city) {
        super(String.format(MSG, city), HttpStatus.BAD_REQUEST);
    }
}