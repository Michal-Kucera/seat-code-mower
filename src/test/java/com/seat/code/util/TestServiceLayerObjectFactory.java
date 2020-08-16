package com.seat.code.util;

import java.time.LocalDateTime;
import java.util.UUID;

import com.seat.code.service.model.Plateau;

public class TestServiceLayerObjectFactory {

    private TestServiceLayerObjectFactory() {
        // this class is not meant to be instantiated as it's a util class
    }

    public static Plateau buildPlateau() {
        final Plateau plateau = new Plateau();
        plateau.setId(UUID.randomUUID());
        plateau.setCreatedDateTime(LocalDateTime.now());
        plateau.setLength(5);
        plateau.setWidth(5);
        plateau.setName("SEAT Martorell Factory");
        return plateau;
    }
}
