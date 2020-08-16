package com.seat.code.domain.entity;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name = "mower")
public class MowerEntity extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(optional = false, fetch = LAZY)
    @JoinColumn(name = "plateau_id", nullable = false, updatable = false)
    private PlateauEntity plateau;

    @OneToMany(cascade = ALL, mappedBy = "mower")
    @OrderBy("created_date_time")
    private final Set<MowerPositionEntity> positions = new HashSet<>();

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

    public Set<MowerPositionEntity> getPositions() {
        return positions;
    }
}
