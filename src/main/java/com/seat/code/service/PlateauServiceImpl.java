package com.seat.code.service;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.seat.code.domain.entity.PlateauEntity;
import com.seat.code.domain.repository.PlateauRepository;
import com.seat.code.service.mapper.PlateauMapper;
import com.seat.code.service.model.Plateau;

@Service
class PlateauServiceImpl implements PlateauService {

    private static final Logger logger = getLogger(PlateauServiceImpl.class);

    private final PlateauMapper plateauMapper;
    private final PlateauRepository plateauRepository;

    PlateauServiceImpl(final PlateauMapper plateauMapper,
                       final PlateauRepository plateauRepository) {
        this.plateauMapper = plateauMapper;
        this.plateauRepository = plateauRepository;
    }

    @Override
    public UUID createPlateau(final Plateau plateau) {
        logger.info("Creating plateau with name: [{}], size: [{}x{}]", plateau.getName(), plateau.getLength(), plateau.getWidth());
        final PlateauEntity plateauEntity = plateauMapper.mapToPlateauEntity(plateau);
        final UUID newPlateauId = plateauRepository.save(plateauEntity).getId();
        logger.info("Plateau with ID: [{}] has been created", newPlateauId);
        return newPlateauId;
    }
}
