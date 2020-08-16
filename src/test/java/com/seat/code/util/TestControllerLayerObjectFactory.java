package com.seat.code.util;

import static com.seat.code.controller.model.MowerOrientation.SOUTH;

import java.util.UUID;

import com.seat.code.controller.model.Mower;
import com.seat.code.controller.model.MowerPosition;
import com.seat.code.controller.model.RectangularPlateau;
import com.seat.code.controller.model.RectangularPlateauDetail;
import com.seat.code.controller.model.RectangularPlateauSize;

public class TestControllerLayerObjectFactory {

    private TestControllerLayerObjectFactory() {
        // this class is not meant to be instantiated as it's a util class
    }

    public static RectangularPlateau buildRectangularPlateau() {
        final RectangularPlateauSize rectangularPlateauSize = new RectangularPlateauSize();
        rectangularPlateauSize.setLength(5);
        rectangularPlateauSize.setWidth(5);

        final RectangularPlateau rectangularPlateau = new RectangularPlateau();
        rectangularPlateau.setName("SEAT Martorell Factory");
        rectangularPlateau.setSize(rectangularPlateauSize);
        return rectangularPlateau;
    }

    public static RectangularPlateauDetail buildRectangularPlateauDetail() {
        final RectangularPlateauSize rectangularPlateauSize = new RectangularPlateauSize();
        rectangularPlateauSize.setLength(5);
        rectangularPlateauSize.setWidth(5);

        final RectangularPlateauDetail rectangularPlateauDetail = new RectangularPlateauDetail();
        rectangularPlateauDetail.setName("SEAT Martorell Factory");
        rectangularPlateauDetail.setSize(rectangularPlateauSize);
        rectangularPlateauDetail.getMowers().add(UUID.randomUUID());
        rectangularPlateauDetail.getMowers().add(UUID.randomUUID());
        rectangularPlateauDetail.getMowers().add(UUID.randomUUID());
        rectangularPlateauDetail.getMowers().add(UUID.randomUUID());
        return rectangularPlateauDetail;
    }

    public static Mower buildMower() {
        final Mower mower = new Mower();
        mower.setName("Mower Nr. 1");
        mower.setPosition(buildMowerPosition());
        return mower;
    }

    public static MowerPosition buildMowerPosition() {
        final MowerPosition mowerPosition = new MowerPosition();
        mowerPosition.setLatitude(2);
        mowerPosition.setLongitude(4);
        mowerPosition.setOrientation(SOUTH);
        return mowerPosition;
    }
}
