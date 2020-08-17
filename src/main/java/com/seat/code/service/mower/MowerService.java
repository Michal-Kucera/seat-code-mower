package com.seat.code.service.mower;

import java.util.UUID;

import com.seat.code.service.mower.exception.MowerNotFoundException;
import com.seat.code.service.mower.exception.MowerPositionAlreadyTakenException;
import com.seat.code.service.mower.exception.MowerPositionOutOfRangeException;
import com.seat.code.service.mower.model.Mower;
import com.seat.code.service.plateau.exception.PlateauNotFoundException;

public interface MowerService {

    /**
     * @throws PlateauNotFoundException           Targeting plateau not found in database
     * @throws MowerPositionOutOfRangeException   Mower's position is out of range of targeting plateau
     * @throws MowerPositionAlreadyTakenException Another mower in the same plateau is already in targeting position
     */
    UUID createMower(final UUID plateauId, Mower mower);

    /**
     * @throws MowerNotFoundException Mower not found in database
     */
    Mower getMower(UUID plateauId, UUID mowerId);
}
