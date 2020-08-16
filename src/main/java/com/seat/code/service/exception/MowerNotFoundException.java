package com.seat.code.service.exception;

/**
 * Mower not found in database
 */
public class MowerNotFoundException extends RuntimeException {

    public MowerNotFoundException(final String message) {
        super(message);
    }
}
