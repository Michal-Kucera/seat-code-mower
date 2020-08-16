package com.seat.code.service;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.seat.code.domain.entity.MowerEntity;
import com.seat.code.domain.entity.PlateauEntity;
import com.seat.code.domain.repository.MowerRepository;
import com.seat.code.domain.repository.PlateauRepository;
import com.seat.code.service.exception.MowerPositionOutOfRangeException;
import com.seat.code.service.exception.PlateauNotFoundException;
import com.seat.code.service.mapper.ServiceLayerMapper;
import com.seat.code.service.model.Mower;

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
        logger.info("Creating mower with name: [{}], starting location: [{}, {}], orientation: [{}] in plateau with ID: [{}]", mower.getName(), mower.getLatitude(), mower.getLongitude(), mower.getOrientation(), mower.getPlateauId());
        final PlateauEntity plateauEntity = plateauRepository.findById(mower.getPlateauId())
            .orElseThrow(() -> new PlateauNotFoundException(format("Plateau with ID: [%s] not found", mower.getPlateauId())));
        if (!isMowerPositionInPlateauRange(mower, plateauEntity)) {
            throw new MowerPositionOutOfRangeException("Mower's position is out of range of plateau");
        }
        final MowerEntity mowerEntity = serviceLayerMapper.mapToMowerEntity(mower, plateauEntity);
        final UUID newMowerId = mowerRepository.save(mowerEntity).getId();
        logger.info("Mower with ID: [{}] has been created", newMowerId);
        return newMowerId;
    }

    private boolean isMowerPositionInPlateauRange(final Mower mower, final PlateauEntity plateauEntity) {
        return plateauEntity.getLength() >= mower.getLatitude()
            && plateauEntity.getWidth() >= mower.getLongitude();
    }
}
