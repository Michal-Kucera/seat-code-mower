package com.seat.code.controller.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.seat.code.controller.model.MowerInstruction;

@Component
class MowerInstructionRequestToMowerInstructionConverter implements Converter<MowerInstruction, com.seat.code.service.mower.model.MowerInstruction> {

    @Override
    public com.seat.code.service.mower.model.MowerInstruction convert(final MowerInstruction source) {
        return com.seat.code.service.mower.model.MowerInstruction.valueOf(source.name());
    }
}
