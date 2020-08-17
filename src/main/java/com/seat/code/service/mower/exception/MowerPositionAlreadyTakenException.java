package com.seat.code.service.mower.exception;

/**
 * Another mower in the same plateau is already in targeting position
 */
public class MowerPositionAlreadyTakenException extends RuntimeException {

    public MowerPositionAlreadyTakenException(final String message) {
        super(message);
    }
}
