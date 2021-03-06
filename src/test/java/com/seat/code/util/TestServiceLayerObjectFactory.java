package com.seat.code.util;

import static com.seat.code.service.mower.model.MowerOrientation.SOUTH;

import java.util.UUID;

import com.seat.code.service.mower.model.Mower;
import com.seat.code.service.plateau.model.Plateau;

public class TestServiceLayerObjectFactory {

    private TestServiceLayerObjectFactory() {
        // this class is not meant to be instantiated as it's a util class
    }

    public static Plateau buildPlateau() {
        final Plateau plateau = new Plateau();
        plateau.setId(UUID.randomUUID());
        plateau.setLength(5);
        plateau.setWidth(5);
        plateau.setName("SEAT Martorell Factory");
        plateau.getMowerIds().add(UUID.randomUUID());
        plateau.getMowerIds().add(UUID.randomUUID());
        plateau.getMowerIds().add(UUID.randomUUID());
        plateau.getMowerIds().add(UUID.randomUUID());
        return plateau;
    }

    public static Mower buildMower() {
        return buildMower(5, 5);
    }

    public static Mower buildMower(final Integer latitude, final Integer longitude) {
        final Mower mower = new Mower();
        mower.setId(UUID.randomUUID());
        mower.setName("Mower Nr. 1");
        mower.setPlateauId(UUID.randomUUID());
        mower.setLatitude(latitude);
        mower.setLongitude(longitude);
        mower.setOrientation(SOUTH);
        return mower;
    }
}
