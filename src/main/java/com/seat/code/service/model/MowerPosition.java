package com.seat.code.service.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class MowerPosition {

    private UUID id;
    private LocalDateTime createdDateTime;
    private Mower mower;
    private Integer latitude;
    private Integer longitude;
    private MowerOrientation orientation;

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(final LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Mower getMower() {
        return mower;
    }

    public void setMower(final Mower mower) {
        this.mower = mower;
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
