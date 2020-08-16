package com.seat.code.service.exception;

public class PlateauNotFoundException extends RuntimeException {

    public PlateauNotFoundException(final String message) {
        super(message);
    }
}
