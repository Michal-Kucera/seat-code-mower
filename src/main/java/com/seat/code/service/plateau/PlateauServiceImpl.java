package com.seat.code.service.plateau;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import com.seat.code.domain.entity.PlateauEntity;
import com.seat.code.domain.repository.PlateauRepository;
import com.seat.code.service.plateau.exception.PlateauNotFoundException;
import com.seat.code.service.plateau.model.Plateau;

@Service
class PlateauServiceImpl implements PlateauService {

    private static final Logger logger = getLogger(PlateauServiceImpl.class);

    private final ConversionService conversionService;
    private final PlateauRepository plateauRepository;

    PlateauServiceImpl(final ConversionService conversionService,
                       final PlateauRepository plateauRepository) {
        this.conversionService = conversionService;
        this.plateauRepository = plateauRepository;
    }

    @Override
    public UUID createPlateau(final Plateau plateau) {
        logger.info("Creating plateau with name: [{}], size: [{}x{}]", plateau.getName(), plateau.getLength(), plateau.getWidth());
        final PlateauEntity plateauEntity = conversionService.convert(plateau, PlateauEntity.class);
        final UUID newPlateauId = plateauRepository.save(plateauEntity).getId();
        logger.info("Plateau with ID: [{}] has been created", newPlateauId);
        return newPlateauId;
    }

    @Override
    public Plateau getPlateau(final UUID plateauId) {
        logger.info("Searching for plateau with ID: [{}] in database", plateauId);
        return plateauRepository.findById(plateauId)
            .map(plateau -> conversionService.convert(plateau, Plateau.class))
            .orElseThrow(() -> new PlateauNotFoundException(format("Plateau with ID: [%s] not found", plateauId)));
    }
}
