package com.seat.code.controller.advice;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.ResponseEntity.badRequest;

import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.seat.code.service.mower.exception.MowerNotFoundException;
import com.seat.code.service.mower.exception.MowerPositionAlreadyTakenException;
import com.seat.code.service.mower.exception.MowerPositionOutOfRangeException;
import com.seat.code.service.plateau.exception.PlateauNotFoundException;

@ControllerAdvice
public class ControllerAdviceHandler {

    private static final Logger logger = getLogger(ControllerAdviceHandler.class);

    @ExceptionHandler({
        PlateauNotFoundException.class,
        MowerPositionOutOfRangeException.class,
        MowerPositionAlreadyTakenException.class,
        MowerNotFoundException.class
    })
    public ResponseEntity<String> handleMowerAppExceptions(final Exception e) {
        logger.info("Handling exception", e);
        return badRequest().body(e.getLocalizedMessage());
    }
}
