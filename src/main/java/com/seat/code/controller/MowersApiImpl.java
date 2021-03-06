package com.seat.code.controller;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.core.convert.ConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.seat.code.controller.model.Mower;
import com.seat.code.controller.model.MowerInstruction;
import com.seat.code.service.mower.MowerService;
import com.seat.code.service.mower.instruction.MowerInstructionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

@RestController
@Validated
@Api(value = "Mowers", tags = "Mowers", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
class MowersApiImpl implements MowersApi {

    private final ConversionService conversionService;
    private final MowerService mowerService;
    private final MowerInstructionService mowerInstructionService;

    MowersApiImpl(final ConversionService conversionService,
                  final MowerService mowerService,
                  final MowerInstructionService mowerInstructionService) {
        this.conversionService = conversionService;
        this.mowerService = mowerService;
        this.mowerInstructionService = mowerInstructionService;
    }

    @Override
    public ResponseEntity<Void> createMower(@ApiParam(value = "Plateau's ID", required = true) @PathVariable("plateauId") final UUID plateauId,
                                            @ApiParam(value = "Create mower request body", required = true) @Valid @RequestBody final Mower createMowerRequest) {
        final com.seat.code.service.mower.model.Mower mower = conversionService.convert(createMowerRequest, com.seat.code.service.mower.model.Mower.class);
        final UUID newMowerId = mowerService.createMower(plateauId, mower);
        final URI locationHeaderValue = buildMowerLocationHeaderValue(newMowerId);
        return created(locationHeaderValue).build();
    }

    @Override
    public ResponseEntity<Mower> getMower(@ApiParam(value = "Plateau's ID", required = true) @PathVariable("plateauId") final UUID plateauId,
                                          @ApiParam(value = "Mower's ID", required = true) @PathVariable("mowerId") final UUID mowerId) {
        final com.seat.code.service.mower.model.Mower mower = mowerService.getMower(plateauId, mowerId);
        final Mower mowerResponse = conversionService.convert(mower, Mower.class);
        return ok(mowerResponse);
    }

    @Override
    public ResponseEntity<Void> applyMowerInstructions(@ApiParam(value = "Plateau's ID", required = true) @PathVariable("plateauId") final UUID plateauId, @ApiParam(value = "Mower's ID", required = true) @PathVariable("mowerId") final UUID mowerId,
                                                       @ApiParam(value = "Mower instructions request body", required = true) @Valid @RequestBody final List<MowerInstruction> mowerInstructionRequest) {
        final List<com.seat.code.service.mower.model.MowerInstruction> mowerInstructions = mowerInstructionRequest.stream()
            .map(mowerInstruction -> conversionService.convert(mowerInstruction, com.seat.code.service.mower.model.MowerInstruction.class))
            .collect(toList());
        mowerInstructionService.applyInstructionsToMower(mowerInstructions, plateauId, mowerId);
        return noContent().build();
    }

    public URI buildMowerLocationHeaderValue(final UUID newMowerId) {
        return fromCurrentRequest()
            .path("/{mowerId}")
            .buildAndExpand(newMowerId)
            .toUri();
    }
}
