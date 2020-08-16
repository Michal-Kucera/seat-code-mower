package com.seat.code.service;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.seat.code.domain.entity.PlateauEntity;
import com.seat.code.domain.repository.PlateauRepository;
import com.seat.code.service.exception.PlateauNotFoundException;
import com.seat.code.service.mapper.ServiceLayerMapper;
import com.seat.code.service.model.Plateau;

@Service
class PlateauServiceImpl implements PlateauService {

    private static final Logger logger = getLogger(PlateauServiceImpl.class);

    private final ServiceLayerMapper serviceLayerMapper;
    private final PlateauRepository plateauRepository;

    PlateauServiceImpl(final ServiceLayerMapper serviceLayerMapper,
                       final PlateauRepository plateauRepository) {
        this.serviceLayerMapper = serviceLayerMapper;
        this.plateauRepository = plateauRepository;
    }

    @Override
    public UUID createPlateau(final Plateau plateau) {
        logger.info("Creating plateau with name: [{}], size: [{}x{}]", plateau.getName(), plateau.getLength(), plateau.getWidth());
        final PlateauEntity plateauEntity = serviceLayerMapper.mapToPlateauEntity(plateau);
        final UUID newPlateauId = plateauRepository.save(plateauEntity).getId();
        logger.info("Plateau with ID: [{}] has been created", newPlateauId);
        return newPlateauId;
    }

    @Override
    public Plateau getPlateau(final UUID plateauId) {
        logger.info("Searching for plateau with ID: [{}] in database", plateauId);
        return plateauRepository.findById(plateauId)
            .map(serviceLayerMapper::mapToPlateau)
            .orElseThrow(() -> new PlateauNotFoundException(format("Plateau with ID: [%s] not found", plateauId)));
    }
}
