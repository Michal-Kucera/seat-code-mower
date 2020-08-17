package com.seat.code.controller.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.seat.code.controller.model.Mower;

@Component
class MowerRequestToMowerConverter implements Converter<Mower, com.seat.code.service.mower.model.Mower> {

    @Override
    public com.seat.code.service.mower.model.Mower convert(final Mower mowerRequest) {
        final com.seat.code.service.mower.model.Mower mower = new com.seat.code.service.mower.model.Mower();
        mower.setName(mowerRequest.getName());
        mower.setLatitude(mowerRequest.getPosition().getLatitude());
        mower.setLongitude(mowerRequest.getPosition().getLongitude());
        mower.setOrientation(com.seat.code.service.mower.model.MowerOrientation.valueOf(mowerRequest.getPosition().getOrientation().name()));
        return mower;
    }
}
