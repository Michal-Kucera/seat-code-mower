package com.seat.code.service.mower.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.seat.code.domain.entity.MowerEntity;
import com.seat.code.domain.entity.MowerEntityOrientation;
import com.seat.code.service.mower.model.Mower;

@Component
class MowerToMowerEntityConverter implements Converter<Mower, MowerEntity> {

    @Override
    public MowerEntity convert(final Mower mower) {
        final MowerEntity mowerEntity = new MowerEntity();
        mowerEntity.setName(mower.getName());
        mowerEntity.setLatitude(mower.getLatitude());
        mowerEntity.setLongitude(mower.getLongitude());
        mowerEntity.setOrientation(MowerEntityOrientation.valueOf(mower.getOrientation().name()));
        return mowerEntity;
    }
}
