package com.seat.code.service;

import java.util.UUID;

import com.seat.code.service.exception.PlateauNotFoundException;
import com.seat.code.service.model.Plateau;

public interface PlateauService {

    UUID createPlateau(Plateau plateau);

    /**
     * @throws PlateauNotFoundException Targeting plateau not found in database
     */
    Plateau getPlateau(UUID plateauId);
}
