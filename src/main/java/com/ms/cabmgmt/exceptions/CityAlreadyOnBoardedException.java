package com.ms.cabmgmt.exceptions;

import org.springframework.http.HttpStatus;

public class CityAlreadyOnBoardedException extends CabServiceException {

    private static final String MSG = "%s city is already onboarded.";

    public CityAlreadyOnBoardedException(String city) {
        super(String.format(MSG, city), HttpStatus.BAD_REQUEST);
    }
}