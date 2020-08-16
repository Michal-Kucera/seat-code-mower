package com.seat.code.service.exception;

/**
 * Mower's position is out of range of targeting plateau
 */
public class MowerPositionOutOfRangeException extends RuntimeException {

    public MowerPositionOutOfRangeException(final String message) {
        super(message);
    }
}
