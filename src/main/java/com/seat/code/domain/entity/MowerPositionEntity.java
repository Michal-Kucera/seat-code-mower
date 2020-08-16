package com.seat.code.domain.entity;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "mower_position")
public class MowerPositionEntity extends BaseEntity {

    @ManyToOne(optional = false, fetch = LAZY)
    @JoinColumn(name = "mower_id", nullable = false, updatable = false)
    private MowerEntity mower;

    @Column(name = "latitude", nullable = false)
    private Integer latitude;

    @Column(name = "longitude", nullable = false)
    private Integer longitude;

    @Column(name = "orientation", nullable = false)
    @Enumerated(STRING)
    private MowerEntityOrientation orientation;

    public MowerEntity getMower() {
        return mower;
    }

    public void setMower(final MowerEntity mower) {
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

    public MowerEntityOrientation getOrientation() {
        return orientation;
    }

    public void setOrientation(final MowerEntityOrientation orientation) {
        this.orientation = orientation;
    }
}
