package com.seat.code.service;

import java.util.UUID;

import com.seat.code.service.exception.MowerPositionAlreadyTakenException;
import com.seat.code.service.exception.MowerPositionOutOfRangeException;
import com.seat.code.service.exception.PlateauNotFoundException;
import com.seat.code.service.model.Mower;

public interface MowerService {

    /**
     * @throws PlateauNotFoundException           Targeting plateau not found in database
     * @throws MowerPositionOutOfRangeException   Mower's position is out of range of targeting plateau
     * @throws MowerPositionAlreadyTakenException Another mower in the same plateau is already in targeting position
     */
    UUID createMower(Mower mower);
}
