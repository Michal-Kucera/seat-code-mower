package com.seat.code.service.plateau.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.seat.code.domain.entity.BaseEntity;
import com.seat.code.domain.entity.PlateauEntity;
import com.seat.code.service.plateau.model.Plateau;

@Component
class PlateauEntityToPlateauConverter implements Converter<PlateauEntity, Plateau> {

    @Override
    public Plateau convert(final PlateauEntity plateauEntity) {
        final Plateau plateau = new Plateau();
        plateau.setName(plateauEntity.getName());
        plateau.setLength(plateauEntity.getLength());
        plateau.setWidth(plateauEntity.getWidth());
        plateau.setId(plateauEntity.getId());
        plateauEntity.getMowers().stream()
            .map(BaseEntity::getId)
            .forEach(plateau.getMowerIds()::add);
        return plateau;
    }
}
