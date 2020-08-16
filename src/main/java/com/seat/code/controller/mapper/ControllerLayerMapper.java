package com.seat.code.controller.mapper;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.seat.code.controller.model.RectangularPlateau;
import com.seat.code.controller.model.RectangularPlateauDetail;
import com.seat.code.controller.model.RectangularPlateauSize;
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
}
