package com.seat.code.util;

import java.time.LocalDateTime;
import java.util.UUID;

import com.seat.code.domain.entity.MowerEntity;
import com.seat.code.domain.entity.PlateauEntity;

public class TestDomainLayerObjectFactory {

    private TestDomainLayerObjectFactory() {
        // this class is not meant to be instantiated as it's a util class
    }

    public static PlateauEntity buildPlateauEntity() {
        final PlateauEntity plateau = new PlateauEntity();
        plateau.setId(UUID.randomUUID());
        plateau.setCreatedDateTime(LocalDateTime.now());
        plateau.setLength(5);
        plateau.setWidth(5);
        plateau.setName("SEAT Martorell Factory");
        plateau.getMowers().add(buildMowerEntity(plateau));
        plateau.getMowers().add(buildMowerEntity(plateau));
        plateau.getMowers().add(buildMowerEntity(plateau));
        plateau.getMowers().add(buildMowerEntity(plateau));
        return plateau;
    }

    private static MowerEntity buildMowerEntity(final PlateauEntity plateauEntity) {
        final MowerEntity mowerEntity = new MowerEntity();
        mowerEntity.setName("Mower Nr. 1");
        mowerEntity.setPlateau(plateauEntity);
        mowerEntity.setId(UUID.randomUUID());
        mowerEntity.setCreatedDateTime(LocalDateTime.now());
        return mowerEntity;
    }
}
