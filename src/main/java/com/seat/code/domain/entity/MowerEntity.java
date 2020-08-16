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
@Table(name = "mower")
public class MowerEntity extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(optional = false, fetch = LAZY)
    @JoinColumn(name = "plateau_id", nullable = false, updatable = false)
    private PlateauEntity plateau;

    @Column(name = "latitude", nullable = false)
    private Integer latitude;

    @Column(name = "longitude", nullable = false)
    private Integer longitude;

    @Column(name = "orientation", nullable = false)
    @Enumerated(STRING)
    private MowerEntityOrientation orientation;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public PlateauEntity getPlateau() {
        return plateau;
    }

    public void setPlateau(final PlateauEntity plateau) {
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

    public MowerEntityOrientation getOrientation() {
        return orientation;
    }

    public void setOrientation(final MowerEntityOrientation orientation) {
        this.orientation = orientation;
    }
}
