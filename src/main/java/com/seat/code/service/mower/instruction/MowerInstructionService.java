package com.seat.code.service.mower.instruction;

import java.util.List;
import java.util.UUID;

import com.seat.code.service.mower.exception.MowerNotFoundException;
import com.seat.code.service.mower.model.MowerInstruction;

public interface MowerInstructionService {

    /**
     * @throws MowerNotFoundException Mower not found in database
     */
    void applyInstructionsToMower(List<MowerInstruction> mowerInstructions, UUID plateauId, UUID mowerId);
}
