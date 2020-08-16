package com.seat.code.controller.mapper;

import org.springframework.stereotype.Component;

import com.seat.code.controller.model.RectangularPlateau;
import com.seat.code.service.model.Plateau;

@Component
public class RectangularPlateauMapper {

    public Plateau mapToPlateau(final RectangularPlateau rectangularPlateau) {
        final Plateau plateau = new Plateau();
        plateau.setName(rectangularPlateau.getName());
        plateau.setLength(rectangularPlateau.getSize().getLength());
        plateau.setWidth(rectangularPlateau.getSize().getWidth());
        return plateau;
    }
}
