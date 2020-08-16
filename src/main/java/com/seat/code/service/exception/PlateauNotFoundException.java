package com.seat.code.service.exception;

/**
 * Plateau not found in database
 */
public class PlateauNotFoundException extends RuntimeException {

    public PlateauNotFoundException(final String message) {
        super(message);
    }
}
