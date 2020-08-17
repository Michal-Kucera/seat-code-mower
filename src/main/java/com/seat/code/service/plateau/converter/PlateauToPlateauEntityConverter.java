package com.seat.code.service.plateau.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.seat.code.domain.entity.PlateauEntity;
import com.seat.code.service.plateau.model.Plateau;

@Component
class PlateauToPlateauEntityConverter implements Converter<Plateau, PlateauEntity> {

    @Override
    public PlateauEntity convert(final Plateau plateau) {
        final PlateauEntity plateauEntity = new PlateauEntity();
        plateauEntity.setName(plateau.getName());
        plateauEntity.setLength(plateau.getLength());
        plateauEntity.setWidth(plateau.getWidth());
        plateauEntity.setId(plateau.getId());
        return plateauEntity;
    }
}
