package com.seat.code.service.mapper;

import org.springframework.stereotype.Component;

import com.seat.code.domain.entity.PlateauEntity;
import com.seat.code.service.model.Plateau;

@Component
public class PlateauMapper {

    public PlateauEntity mapToPlateauEntity(final Plateau plateau) {
        final PlateauEntity plateauEntity = new PlateauEntity();
        plateauEntity.setName(plateau.getName());
        plateauEntity.setLength(plateau.getLength());
        plateauEntity.setWidth(plateau.getWidth());
        plateauEntity.setId(plateau.getId());
        plateauEntity.setCreatedDateTime(plateau.getCreatedDateTime());
        return plateauEntity;
    }
}
