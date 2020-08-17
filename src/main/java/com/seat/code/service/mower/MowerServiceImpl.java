package com.seat.code.service.mower;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import com.seat.code.domain.entity.MowerEntity;
import com.seat.code.domain.entity.PlateauEntity;
import com.seat.code.domain.repository.MowerRepository;
import com.seat.code.domain.repository.PlateauRepository;
import com.seat.code.service.mower.exception.MowerNotFoundException;
import com.seat.code.service.mower.exception.MowerPositionAlreadyTakenException;
import com.seat.code.service.mower.exception.MowerPositionOutOfRangeException;
import com.seat.code.service.mower.model.Mower;
import com.seat.code.service.plateau.exception.PlateauNotFoundException;

@Service
class MowerServiceImpl implements MowerService {

    private static final Logger logger = getLogger(MowerServiceImpl.class);

    private final ConversionService conversionService;
    private final PlateauRepository plateauRepository;
    private final MowerRepository mowerRepository;

    MowerServiceImpl(final ConversionService conversionService,
                     final PlateauRepository plateauRepository,
                     final MowerRepository mowerRepository) {
        this.conversionService = conversionService;
        this.plateauRepository = plateauRepository;
        this.mowerRepository = mowerRepository;
    }

    @Override
    public UUID createMower(final UUID plateauId, final Mower mower) {
        logger.info("Creating mower with name: [{}], starting location: [{}, {}], orientation: [{}] in plateau with ID: [{}]", mower.getName(), mower.getLongitude(), mower.getLatitude(), mower.getOrientation(), plateauId);
        final PlateauEntity plateauEntity = plateauRepository.findById(plateauId)
            .orElseThrow(() -> new PlateauNotFoundException(format("Plateau with ID: [%s] not found", plateauId)));

        if (!isMowerPositionInPlateauRange(mower, plateauEntity)) {
            throw new MowerPositionOutOfRangeException("Mower's position is out of range of plateau");
        }

        if (isMowerTargetingPositionAlreadyTakenInPlateau(mower, plateauEntity)) {
            throw new MowerPositionAlreadyTakenException("Mower's targeting position is already taken by another mower in plateau");
        }

        final MowerEntity mowerEntity = conversionService.convert(mower, MowerEntity.class);
        mowerEntity.setPlateau(plateauEntity);
        final UUID newMowerId = mowerRepository.save(mowerEntity).getId();
        logger.info("Mower with ID: [{}] has been created", newMowerId);
        return newMowerId;
    }

    @Override
    public Mower getMower(final UUID plateauId, final UUID mowerId) {
        logger.info("Searching for mower with ID: [{}], associated to plateau with ID: [{}] in database", mowerId, plateauId);
        return mowerRepository.findOneByIdAndPlateauId(mowerId, plateauId)
            .map(mower -> conversionService.convert(mower, Mower.class))
            .orElseThrow(() -> new MowerNotFoundException(format("Mower with ID: [%s], associated to plateau with ID: [%s] not found", mowerId, plateauId)));
    }

    private boolean isMowerPositionInPlateauRange(final Mower mower, final PlateauEntity plateauEntity) {
        return plateauEntity.getLength() >= mower.getLatitude()
            && plateauEntity.getWidth() >= mower.getLongitude();
    }

    private boolean isMowerTargetingPositionAlreadyTakenInPlateau(final Mower mower,
                                                                  final PlateauEntity plateauEntity) {
        return plateauEntity.getMowers().stream()
            .anyMatch(mowerInPlateau -> mowerInPlateau.getLatitude().equals(mower.getLatitude())
                && mowerInPlateau.getLongitude().equals(mower.getLongitude()));
    }
}
