package com.seat.code.service.mower.instruction.processor;

import static com.seat.code.domain.entity.MowerEntityOrientation.EAST;
import static com.seat.code.domain.entity.MowerEntityOrientation.NORTH;
import static com.seat.code.domain.entity.MowerEntityOrientation.SOUTH;
import static com.seat.code.domain.entity.MowerEntityOrientation.WEST;
import static com.seat.code.service.mower.model.MowerInstruction.SPIN_LEFT;
import static java.util.Collections.unmodifiableMap;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.EnumMap;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.seat.code.domain.entity.MowerEntity;
import com.seat.code.domain.entity.MowerEntityOrientation;
import com.seat.code.service.mower.model.MowerInstruction;

@Component
class MowerSpinLeftInstructionProcessor implements MowerInstructionProcessor {

    private static final Logger logger = getLogger(MowerSpinLeftInstructionProcessor.class);

    private static final Map<MowerEntityOrientation, MowerEntityOrientation> NEW_ORIENTATION_MAP;

    static {
        final Map<MowerEntityOrientation, MowerEntityOrientation> newOrientationMap = new EnumMap<>(MowerEntityOrientation.class);
        newOrientationMap.put(NORTH, WEST);
        newOrientationMap.put(EAST, NORTH);
        newOrientationMap.put(SOUTH, EAST);
        newOrientationMap.put(WEST, SOUTH);
        NEW_ORIENTATION_MAP = unmodifiableMap(newOrientationMap);
    }

    @Override
    public boolean isInstructionSupported(final MowerInstruction instruction) {
        return instruction == SPIN_LEFT;
    }

    @Override
    public void applyInstruction(final MowerEntity mower) {
        final MowerEntityOrientation newOrientation = NEW_ORIENTATION_MAP.get(mower.getOrientation());
        logger.info("Mowing mower with ID: [{}] to left, from: [{}], to: [{}]", mower.getId(), mower.getOrientation(), newOrientation);
        mower.setOrientation(newOrientation);
    }
}
