package com.seat.code.service.mower.instruction.processor;

import static com.seat.code.service.mower.model.MowerInstruction.MOVE_FORWARD;
import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.seat.code.domain.entity.MowerEntity;
import com.seat.code.domain.entity.PlateauEntity;
import com.seat.code.service.mower.exception.MowerPositionAlreadyTakenException;
import com.seat.code.service.mower.exception.MowerPositionOutOfRangeException;
import com.seat.code.service.mower.model.MowerInstruction;

@Component
class MowerMoveForwardInstructionProcessor implements MowerInstructionProcessor {

    private static final Logger logger = getLogger(MowerMoveForwardInstructionProcessor.class);

    private static final int MINIMUM_LONGITUDE = 0;
    private static final int MINIMUM_LATITUDE = 0;

    @Override
    public boolean isInstructionSupported(final MowerInstruction instruction) {
        return instruction == MOVE_FORWARD;
    }

    /**
     * @throws MowerPositionOutOfRangeException   Mower's position is out of range of targeting plateau
     * @throws MowerPositionAlreadyTakenException Another mower in the same plateau is already in targeting position
     */
    @Override
    public void applyInstruction(final MowerEntity mower) {
        Integer newLatitude = mower.getLatitude();
        Integer newLongitude = mower.getLongitude();
        switch (mower.getOrientation()) {
            case NORTH:
                newLatitude++;
                break;

            case EAST:
                newLongitude++;
                break;

            case SOUTH:
                newLatitude--;
                break;

            case WEST:
                newLongitude--;
                break;
        }

        if (!isMowerPositionInPlateauRange(newLatitude, newLongitude, mower.getPlateau())) {
            throw new MowerPositionOutOfRangeException("Mower instruction to move forward cannot be applied because mower would go out of the range of plateau");
        }

        if (isMowerTargetingPositionAlreadyTakenInPlateau(newLatitude, newLongitude, mower)) {
            throw new MowerPositionAlreadyTakenException("Mower's targeting position is already taken by another mower in plateau");
        }

        logger.info("Mowing mower with ID: [{}] forward, from: [{} {}], to: [{} {}], orientation: [{}]", mower.getId(), mower.getLongitude(), mower.getLatitude(), newLongitude, newLatitude, mower.getOrientation());
        mower.setLatitude(newLatitude);
        mower.setLongitude(newLongitude);
    }

    private boolean isMowerPositionInPlateauRange(final Integer mowerLatitude,
                                                  final Integer mowerLongitude,
                                                  final PlateauEntity plateauEntity) {
        return plateauEntity.getLength() >= mowerLatitude
            && plateauEntity.getWidth() >= mowerLongitude
            && mowerLatitude >= MINIMUM_LATITUDE
            && mowerLongitude >= MINIMUM_LONGITUDE;
    }

    private boolean isMowerTargetingPositionAlreadyTakenInPlateau(final Integer mowerLatitude,
                                                                  final Integer mowerLongitude,
                                                                  final MowerEntity mower) {
        return mower.getPlateau()
            .getMowers().stream()
            .anyMatch(mowerInPlateau -> mowerInPlateau.getLatitude().equals(mowerLatitude)
                && mowerInPlateau.getLongitude().equals(mowerLongitude));
    }
}
