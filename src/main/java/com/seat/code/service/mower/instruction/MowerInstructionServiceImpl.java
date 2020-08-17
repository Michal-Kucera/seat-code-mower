package com.seat.code.service.mower.instruction;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.seat.code.domain.entity.MowerEntity;
import com.seat.code.domain.repository.MowerRepository;
import com.seat.code.service.mower.exception.MowerNotFoundException;
import com.seat.code.service.mower.instruction.processor.MowerInstructionProcessor;
import com.seat.code.service.mower.model.MowerInstruction;

@Service
class MowerInstructionServiceImpl implements MowerInstructionService {

    private static final Logger logger = getLogger(MowerInstructionServiceImpl.class);

    private final MowerRepository mowerRepository;
    private final List<MowerInstructionProcessor> mowerInstructionProcessors;

    MowerInstructionServiceImpl(final MowerRepository mowerRepository,
                                final List<MowerInstructionProcessor> mowerInstructionProcessors) {
        this.mowerRepository = mowerRepository;
        this.mowerInstructionProcessors = mowerInstructionProcessors;
    }

    @Override
    public void applyInstructionsToMower(final List<MowerInstruction> mowerInstructions,
                                         final UUID plateauId,
                                         final UUID mowerId) {
        logger.info("Applying instructions: {} to mower with ID: [{}], associated to plateau with ID: [{}]", mowerInstructions, mowerId, plateauId);
        final MowerEntity mowerEntity = mowerRepository.findOneByIdAndPlateauId(mowerId, plateauId)
            .orElseThrow(() -> new MowerNotFoundException(format("Mower with ID: [%s], associated to plateau with ID: [%s] not found", mowerId, plateauId)));
        mowerInstructions.stream()
            .map(this::getInstructionProcessors)
            .flatMap(Collection::stream)
            .forEach(instructionProcessor -> instructionProcessor.applyInstruction(mowerEntity));
        logger.info("Instructions have been successfully applied, final location of mower: [{}, {}], orientation: [{}]", mowerEntity.getLongitude(), mowerEntity.getLatitude(), mowerEntity.getOrientation());
        mowerRepository.save(mowerEntity);
    }

    private List<MowerInstructionProcessor> getInstructionProcessors(final MowerInstruction mowerInstruction) {
        return mowerInstructionProcessors.stream()
            .filter(instructionProcessor -> instructionProcessor.isInstructionSupported(mowerInstruction))
            .collect(toList());
    }
}
