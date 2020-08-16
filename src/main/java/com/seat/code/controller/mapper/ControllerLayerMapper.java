package com.seat.code.controller.mapper;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.seat.code.controller.model.Mower;
import com.seat.code.controller.model.RectangularPlateau;
import com.seat.code.controller.model.RectangularPlateauDetail;
import com.seat.code.controller.model.RectangularPlateauSize;
import com.seat.code.service.model.MowerOrientation;
import com.seat.code.service.model.Plateau;

@Component
public class ControllerLayerMapper {

    public Plateau mapToPlateau(final RectangularPlateau rectangularPlateau) {
        final Plateau plateau = new Plateau();
        plateau.setName(rectangularPlateau.getName());
        plateau.setLength(rectangularPlateau.getSize().getLength());
        plateau.setWidth(rectangularPlateau.getSize().getWidth());
        return plateau;
    }

    public RectangularPlateauDetail mapToPlateauDetail(final Plateau plateau) {
        final RectangularPlateauSize rectangularPlateauSize = new RectangularPlateauSize();
        rectangularPlateauSize.setLength(plateau.getLength());
        rectangularPlateauSize.setWidth(plateau.getWidth());
        final RectangularPlateauDetail rectangularPlateauDetail = new RectangularPlateauDetail();
        rectangularPlateauDetail.setName(plateau.getName());
        rectangularPlateauDetail.setSize(rectangularPlateauSize);
        rectangularPlateauDetail.setMowers(new ArrayList<>(plateau.getMowerIds()));
        return rectangularPlateauDetail;
    }

    public com.seat.code.service.model.Mower mapToMower(final UUID plateauId, final Mower mowerRequest) {
        final com.seat.code.service.model.Mower mower = new com.seat.code.service.model.Mower();
        mower.setName(mowerRequest.getName());
        mower.setPlateauId(plateauId);
        mower.setLatitude(mowerRequest.getPosition().getLatitude());
        mower.setLongitude(mowerRequest.getPosition().getLongitude());
        mower.setOrientation(MowerOrientation.valueOf(mowerRequest.getPosition().getOrientation().name()));
        return mower;
    }
}
