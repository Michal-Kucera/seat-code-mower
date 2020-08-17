package com.seat.code.service.mower;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.seat.code.domain.entity.MowerEntity;
import com.seat.code.domain.entity.PlateauEntity;
import com.seat.code.domain.repository.MowerRepository;
import com.seat.code.domain.repository.PlateauRepository;
import com.seat.code.service.mapper.ServiceLayerMapper;
import com.seat.code.service.mower.exception.MowerNotFoundException;
import com.seat.code.service.mower.exception.MowerPositionAlreadyTakenException;
import com.seat.code.service.mower.exception.MowerPositionOutOfRangeException;
import com.seat.code.service.mower.model.Mower;
import com.seat.code.service.plateau.exception.PlateauNotFoundException;

@Service
class MowerServiceImpl implements MowerService {

    private static final Logger logger = getLogger(MowerServiceImpl.class);

    private final ServiceLayerMapper serviceLayerMapper;
    private final PlateauRepository plateauRepository;
    private final MowerRepository mowerRepository;

    MowerServiceImpl(final ServiceLayerMapper serviceLayerMapper,
                     final PlateauRepository plateauRepository,
                     final MowerRepository mowerRepository) {
        this.serviceLayerMapper = serviceLayerMapper;
        this.plateauRepository = plateauRepository;
        this.mowerRepository = mowerRepository;
    }

    @Override
    public UUID createMower(final Mower mower) {
        logger.info("Creating mower with name: [{}], starting location: [{}, {}], orientation: [{}] in plateau with ID: [{}]", mower.getName(), mower.getLongitude(), mower.getLatitude(), mower.getOrientation(), mower.getPlateauId());
        final PlateauEntity plateauEntity = plateauRepository.findById(mower.getPlateauId())
            .orElseThrow(() -> new PlateauNotFoundException(format("Plateau with ID: [%s] not found", mower.getPlateauId())));

        if (!isMowerPositionInPlateauRange(mower, plateauEntity)) {
            throw new MowerPositionOutOfRangeException("Mower's position is out of range of plateau");
        }

        if (isMowerTargetingPositionAlreadyTakenInPlateau(mower, plateauEntity)) {
            throw new MowerPositionAlreadyTakenException("Mower's targeting position is already taken by another mower in plateau");
        }

        final MowerEntity mowerEntity = serviceLayerMapper.mapToMowerEntity(mower, plateauEntity);
        final UUID newMowerId = mowerRepository.save(mowerEntity).getId();
        logger.info("Mower with ID: [{}] has been created", newMowerId);
        return newMowerId;
    }

    @Override
    public Mower getMower(final UUID plateauId, final UUID mowerId) {
        logger.info("Searching for mower with ID: [{}], associated to plateau with ID: [{}] in database", mowerId, plateauId);
        return mowerRepository.findOneByIdAndPlateauId(mowerId, plateauId)
            .map(serviceLayerMapper::mapToMower)
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
