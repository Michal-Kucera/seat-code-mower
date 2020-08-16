package com.seat.code.service.mapper;

import org.springframework.stereotype.Component;

import com.seat.code.domain.entity.BaseEntity;
import com.seat.code.domain.entity.MowerEntity;
import com.seat.code.domain.entity.MowerEntityOrientation;
import com.seat.code.domain.entity.PlateauEntity;
import com.seat.code.service.model.Mower;
import com.seat.code.service.model.Plateau;

@Component
public class ServiceLayerMapper {

    public Plateau mapToPlateau(final PlateauEntity plateauEntity) {
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

    public PlateauEntity mapToPlateauEntity(final Plateau plateau) {
        final PlateauEntity plateauEntity = new PlateauEntity();
        plateauEntity.setName(plateau.getName());
        plateauEntity.setLength(plateau.getLength());
        plateauEntity.setWidth(plateau.getWidth());
        plateauEntity.setId(plateau.getId());
        return plateauEntity;
    }

    public MowerEntity mapToMowerEntity(final Mower mower, final PlateauEntity plateauEntity) {
        final MowerEntity mowerEntity = new MowerEntity();
        mowerEntity.setName(mower.getName());
        mowerEntity.setPlateau(plateauEntity);
        mowerEntity.setLatitude(mower.getLatitude());
        mowerEntity.setLongitude(mower.getLongitude());
        mowerEntity.setOrientation(MowerEntityOrientation.valueOf(mower.getOrientation().name()));
        return mowerEntity;
    }
}
