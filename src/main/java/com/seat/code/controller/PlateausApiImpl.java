package com.seat.code.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import java.net.URI;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.core.convert.ConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.seat.code.controller.model.RectangularPlateau;
import com.seat.code.controller.model.RectangularPlateauDetail;
import com.seat.code.service.plateau.PlateauService;
import com.seat.code.service.plateau.model.Plateau;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

@RestController
@Validated
@Api(value = "Plateaus", tags = "Plateaus", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
class PlateausApiImpl implements PlateausApi {

    private final ConversionService conversionService;
    private final PlateauService plateauService;

    PlateausApiImpl(final ConversionService conversionService,
                    final PlateauService plateauService) {
        this.conversionService = conversionService;
        this.plateauService = plateauService;
    }

    @Override
    public ResponseEntity<Void> createPlateau(@ApiParam(value = "Create plateau request body", required = true) @Valid @RequestBody final RectangularPlateau createPlateauRequest) {
        final Plateau plateau = conversionService.convert(createPlateauRequest, Plateau.class);
        final UUID newPlateauId = plateauService.createPlateau(plateau);
        final URI locationHeaderValue = buildPlateauLocationHeaderValue(newPlateauId);
        return created(locationHeaderValue).build();
    }

    @Override
    public ResponseEntity<RectangularPlateauDetail> getPlateau(@ApiParam(value = "Plateau's ID", required = true) @PathVariable("plateauId") final UUID plateauId) {
        final Plateau plateau = plateauService.getPlateau(plateauId);
        final RectangularPlateauDetail plateauDetail = conversionService.convert(plateau, RectangularPlateauDetail.class);
        return ok(plateauDetail);
    }

    public URI buildPlateauLocationHeaderValue(final UUID newPlateauId) {
        return fromCurrentRequest()
            .path("/{plateauId}")
            .buildAndExpand(newPlateauId)
            .toUri();
    }
}
