package com.seat.code.service.mower.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.seat.code.domain.entity.MowerEntity;
import com.seat.code.service.mower.model.Mower;
import com.seat.code.service.mower.model.MowerOrientation;

@Component
class MowerEntityToMowerConverter implements Converter<MowerEntity, Mower> {

    @Override
    public Mower convert(final MowerEntity mowerEntity) {
        final Mower mower = new Mower();
        mower.setId(mowerEntity.getId());
        mower.setName(mowerEntity.getName());
        mower.setPlateauId(mowerEntity.getPlateau().getId());
        mower.setLatitude(mowerEntity.getLatitude());
        mower.setLongitude(mowerEntity.getLongitude());
        mower.setOrientation(MowerOrientation.valueOf(mowerEntity.getOrientation().name()));
        return mower;
    }
}
