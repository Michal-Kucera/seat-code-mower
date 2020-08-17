package com.seat.code.service.plateau;

import java.util.UUID;

import com.seat.code.service.plateau.exception.PlateauNotFoundException;
import com.seat.code.service.plateau.model.Plateau;

public interface PlateauService {

    UUID createPlateau(Plateau plateau);

    /**
     * @throws PlateauNotFoundException Targeting plateau not found in database
     */
    Plateau getPlateau(UUID plateauId);
}
