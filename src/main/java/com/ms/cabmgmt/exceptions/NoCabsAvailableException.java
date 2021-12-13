package com.ms.cabmgmt.exceptions;

public class NoCabsAvailableException extends CabServiceException {

    private static final String MSG = "Cabs are not available.";

    public NoCabsAvailableException() {
        super(MSG);
    }
}