package com.seat.code.controller.advice;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.ResponseEntity.badRequest;

import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.seat.code.service.exception.PlateauNotFoundException;

@ControllerAdvice
public class ControllerAdviceHandler {

    private static final Logger logger = getLogger(ControllerAdviceHandler.class);

    @ExceptionHandler(PlateauNotFoundException.class)
    public ResponseEntity<String> handlePlateauNotFoundException(final PlateauNotFoundException e) {
        logger.info("Handling Plateau Not Found exception", e);
        return badRequest().body(e.getLocalizedMessage());
    }
}
