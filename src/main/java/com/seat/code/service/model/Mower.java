package com.seat.code.service.model;

import static java.util.Objects.isNull;

import java.time.LocalDateTime;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

public class Mower {

    private UUID id;
    private LocalDateTime createdDateTime;
    private String name;
    private Plateau plateau;
    private SortedSet<MowerPosition> positions;

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

    public SortedSet<MowerPosition> getPositions() {
        if (isNull(positions)) {
            positions = new TreeSet<>();
        }

        return positions;
    }
}
