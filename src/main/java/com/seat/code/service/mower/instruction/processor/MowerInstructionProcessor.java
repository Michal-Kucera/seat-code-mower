package com.seat.code.service.mower.instruction.processor;

import com.seat.code.domain.entity.MowerEntity;
import com.seat.code.service.mower.model.MowerInstruction;

public interface MowerInstructionProcessor {

    boolean isInstructionSupported(MowerInstruction instruction);

    void applyInstruction(MowerEntity mower);
}
