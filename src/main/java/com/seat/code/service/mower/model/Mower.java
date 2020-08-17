package com.seat.code.service.mower.model;

import java.util.UUID;

public class Mower {

    private UUID id;
    private String name;
    private UUID plateauId;
    private Integer latitude;
    private Integer longitude;
    private MowerOrientation orientation;

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public UUID getPlateauId() {
        return plateauId;
    }

    public void setPlateauId(final UUID plateauId) {
        this.plateauId = plateauId;
    }

    public Integer getLatitude() {
        return latitude;
    }

    public void setLatitude(final Integer latitude) {
        this.latitude = latitude;
    }

    public Integer getLongitude() {
        return longitude;
    }

    public void setLongitude(final Integer longitude) {
        this.longitude = longitude;
    }

    public MowerOrientation getOrientation() {
        return orientation;
    }

    public void setOrientation(final MowerOrientation orientation) {
        this.orientation = orientation;
    }
}
