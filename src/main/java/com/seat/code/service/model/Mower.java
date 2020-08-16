package com.seat.code.service.model;

import java.util.UUID;

public class Mower {

    private UUID id;
    private String name;
    private Plateau plateau;
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

    public Plateau getPlateau() {
        return plateau;
    }

    public void setPlateau(final Plateau plateau) {
        this.plateau = plateau;
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
