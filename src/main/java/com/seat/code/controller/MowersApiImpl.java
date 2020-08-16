package com.seat.code.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.seat.code.controller.mapper.ControllerLayerMapper;
import com.seat.code.controller.model.Mower;
import com.seat.code.controller.model.MowerInstruction;
import com.seat.code.service.MowerService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

@RestController
@Validated
@Api(value = "Mowers", tags = "Mowers", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
class MowersApiImpl implements MowersApi {

    private final ControllerLayerMapper controllerLayerMapper;
    private final MowerService mowerService;

    MowersApiImpl(final ControllerLayerMapper controllerLayerMapper,
                  final MowerService mowerService) {
        this.controllerLayerMapper = controllerLayerMapper;
        this.mowerService = mowerService;
    }

    @Override
    public ResponseEntity<Void> createMower(@ApiParam(value = "Plateau's ID", required = true) @PathVariable("plateauId") final UUID plateauId,
                                            @ApiParam(value = "Create mower request body", required = true) @Valid @RequestBody final Mower createMowerRequest) {
        final com.seat.code.service.model.Mower mower = controllerLayerMapper.mapToMower(plateauId, createMowerRequest);
        final UUID newMowerId = mowerService.createMower(mower);
        final URI locationHeaderValue = buildMowerLocationHeaderValue(newMowerId);
        return created(locationHeaderValue).build();
    }

    @Override
    public ResponseEntity<Mower> getMower(@ApiParam(value = "Plateau's ID", required = true) @PathVariable("plateauId") final UUID plateauId,
                                          @ApiParam(value = "Mower's ID", required = true) @PathVariable("mowerId") final UUID mowerId) {
        final com.seat.code.service.model.Mower mower = mowerService.getMower(plateauId, mowerId);
        final Mower mowerResponse = controllerLayerMapper.mapToMowerResponse(mower);
        return ok(mowerResponse);
    }

    @Override
    public ResponseEntity<Void> applyMowerInstructions(@ApiParam(value = "Plateau's ID", required = true) @PathVariable("plateauId") final UUID plateauId, @ApiParam(value = "Mower's ID", required = true) @PathVariable("mowerId") final UUID mowerId,
                                                       @ApiParam(value = "Mower instructions request body", required = true) @Valid @RequestBody final List<MowerInstruction> mowerInstructionRequest) {
        return noContent().build();
    }

    public URI buildMowerLocationHeaderValue(final UUID newMowerId) {
        return fromCurrentRequest()
            .path("/{mowerId}")
            .buildAndExpand(newMowerId)
            .toUri();
    }
}
