package com.seat.code.controller.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.seat.code.controller.model.RectangularPlateau;
import com.seat.code.service.plateau.model.Plateau;

@Component
class RectangularPlateauToPlateauConverter implements Converter<RectangularPlateau, Plateau> {

    @Override
    public Plateau convert(final RectangularPlateau rectangularPlateau) {
        final Plateau plateau = new Plateau();
        plateau.setName(rectangularPlateau.getName());
        plateau.setLength(rectangularPlateau.getSize().getLength());
        plateau.setWidth(rectangularPlateau.getSize().getWidth());
        return plateau;
    }
}
