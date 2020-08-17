package com.seat.code.controller.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.seat.code.controller.model.Mower;
import com.seat.code.controller.model.MowerOrientation;
import com.seat.code.controller.model.MowerPosition;

@Component
class MowerToMowerResponseConverter implements Converter<com.seat.code.service.mower.model.Mower, Mower> {

    @Override
    public Mower convert(final com.seat.code.service.mower.model.Mower mower) {
        final MowerPosition mowerPosition = new MowerPosition();
        mowerPosition.setLatitude(mower.getLatitude());
        mowerPosition.setLongitude(mower.getLongitude());
        mowerPosition.setOrientation(MowerOrientation.valueOf(mower.getOrientation().name()));

        final Mower mowerResponse = new Mower();
        mowerResponse.setName(mower.getName());
        mowerResponse.setPosition(mowerPosition);
        return mowerResponse;
    }
}
