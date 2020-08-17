package com.seat.code.service.plateau.exception;

/**
 * Plateau not found in database
 */
public class PlateauNotFoundException extends RuntimeException {

    public PlateauNotFoundException(final String message) {
        super(message);
    }
}
