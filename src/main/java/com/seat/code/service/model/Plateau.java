package com.seat.code.service.model;

import static java.util.Objects.isNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Plateau {

    private UUID id;
    private String name;
    private Integer length;
    private Integer width;
    private Set<UUID> mowerIds;

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

    public Integer getLength() {
        return length;
    }

    public void setLength(final Integer length) {
        this.length = length;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(final Integer width) {
        this.width = width;
    }

    public Set<UUID> getMowerIds() {
        if (isNull(mowerIds)) {
            mowerIds = new HashSet<>();
        }

        return mowerIds;
    }
}
