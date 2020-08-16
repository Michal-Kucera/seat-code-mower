package com.seat.code.controller.advice;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.ResponseEntity.badRequest;

import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.seat.code.service.exception.MowerNotFoundException;
import com.seat.code.service.exception.MowerPositionAlreadyTakenException;
import com.seat.code.service.exception.MowerPositionOutOfRangeException;
import com.seat.code.service.exception.PlateauNotFoundException;

@ControllerAdvice
public class ControllerAdviceHandler {

    private static final Logger logger = getLogger(ControllerAdviceHandler.class);

    @ExceptionHandler(PlateauNotFoundException.class)
    public ResponseEntity<String> handlePlateauNotFoundException(final PlateauNotFoundException e) {
        logger.info("Handling Plateau Not Found exception", e);
        return badRequest().body(e.getLocalizedMessage());
    }

    @ExceptionHandler(MowerPositionOutOfRangeException.class)
    public ResponseEntity<String> handleMowerPositionOutOfRangeException(final MowerPositionOutOfRangeException e) {
        logger.info("Handling Mower Position Out Of Range exception", e);
        return badRequest().body(e.getLocalizedMessage());
    }

    @ExceptionHandler(MowerPositionAlreadyTakenException.class)
    public ResponseEntity<String> handleMowerPositionAlreadyTakenException(final MowerPositionAlreadyTakenException e) {
        logger.info("Handling Mower Position Already Taken exception", e);
        return badRequest().body(e.getLocalizedMessage());
    }

    @ExceptionHandler(MowerNotFoundException.class)
    public ResponseEntity<String> handleMowerNotFoundException(final MowerNotFoundException e) {
        logger.info("Handling Mower Not Found exception", e);
        return badRequest().body(e.getLocalizedMessage());
    }
}
