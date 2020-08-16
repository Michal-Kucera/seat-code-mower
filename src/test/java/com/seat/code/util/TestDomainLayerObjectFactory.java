package com.seat.code.util;

import static com.seat.code.domain.entity.MowerEntityOrientation.NORTH;

import java.util.UUID;

import com.seat.code.domain.entity.MowerEntity;
import com.seat.code.domain.entity.PlateauEntity;

public class TestDomainLayerObjectFactory {

    private TestDomainLayerObjectFactory() {
        // this class is not meant to be instantiated as it's a util class
    }

    public static PlateauEntity buildPlateauEntity() {
        return buildPlateauEntity(5, 5);
    }

    public static PlateauEntity buildPlateauEntity(final Integer length, final Integer width) {
        final PlateauEntity plateau = new PlateauEntity();
        plateau.setId(UUID.randomUUID());
        plateau.setLength(length);
        plateau.setWidth(width);
        plateau.setName("SEAT Martorell Factory");
        plateau.getMowers().add(buildMowerEntity(plateau));
        plateau.getMowers().add(buildMowerEntity(plateau));
        plateau.getMowers().add(buildMowerEntity(plateau));
        plateau.getMowers().add(buildMowerEntity(plateau));
        plateau.setVersion(1);
        return plateau;
    }

    public static MowerEntity buildMowerEntity(final PlateauEntity plateauEntity) {
        final MowerEntity mowerEntity = new MowerEntity();
        mowerEntity.setName("Mower Nr. 1");
        mowerEntity.setPlateau(plateauEntity);
        mowerEntity.setId(UUID.randomUUID());
        mowerEntity.setLatitude(11);
        mowerEntity.setLongitude(5);
        mowerEntity.setOrientation(NORTH);
        mowerEntity.setVersion(1);
        return mowerEntity;
    }
}
