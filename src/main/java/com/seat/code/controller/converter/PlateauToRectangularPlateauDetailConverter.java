package com.seat.code.controller.converter;

import java.util.ArrayList;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.seat.code.controller.model.RectangularPlateauDetail;
import com.seat.code.controller.model.RectangularPlateauSize;
import com.seat.code.service.plateau.model.Plateau;

@Component
class PlateauToRectangularPlateauDetailConverter implements Converter<Plateau, RectangularPlateauDetail> {

    @Override
    public RectangularPlateauDetail convert(final Plateau plateau) {
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
