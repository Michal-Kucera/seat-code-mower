package com.seat.code.service.mapper;

import org.springframework.stereotype.Component;

import com.seat.code.domain.entity.BaseEntity;
import com.seat.code.domain.entity.PlateauEntity;
import com.seat.code.service.model.Plateau;

@Component
public class ServiceLayerMapper {

    public Plateau mapToPlateau(final PlateauEntity plateauEntity) {
        final Plateau plateau = new Plateau();
        plateau.setName(plateauEntity.getName());
        plateau.setLength(plateauEntity.getLength());
        plateau.setWidth(plateauEntity.getWidth());
        plateau.setId(plateauEntity.getId());
        plateau.setCreatedDateTime(plateauEntity.getCreatedDateTime());
        plateauEntity.getMowers().stream()
            .map(BaseEntity::getId)
            .forEach(plateau.getMowerIds()::add);
        return plateau;
    }

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
