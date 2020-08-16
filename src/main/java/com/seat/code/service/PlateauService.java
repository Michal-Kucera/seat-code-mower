package com.seat.code.service;

import java.util.UUID;

import com.seat.code.service.model.Plateau;

public interface PlateauService {

    UUID createPlateau(Plateau plateau);

    Plateau getPlateau(UUID plateauId);
}
