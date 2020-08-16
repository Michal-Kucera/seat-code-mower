package com.seat.code.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.seat.code.controller.model.RectangularPlateau;
import com.seat.code.controller.model.RectangularPlateauDetail;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

@RestController
@Validated
@Api(value = "Plateaus", tags = "Plateaus", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
class PlateausApiImpl implements PlateausApi {

    @Override
    public ResponseEntity<Void> createPlateau(@ApiParam(value = "Create plateau request body", required = true) @Valid @RequestBody final RectangularPlateau createPlateauRequest) {
        return created(null).build();
    }

    @Override
    public ResponseEntity<RectangularPlateauDetail> getPlateau(@ApiParam(value = "Plateau's ID", required = true) @PathVariable("plateauId") final UUID plateauId) {
        return ok(null);
    }
}
