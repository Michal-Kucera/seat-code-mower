package com.seat.code.service.model;

import static java.util.Objects.isNull;

import java.time.LocalDateTime;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;

public class Plateau {

    private UUID id;
    private LocalDateTime createdDateTime;
    private String name;
    private Integer length;
    private Integer width;
    private SortedSet<Mower> mowers;

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

    public SortedSet<Mower> getMowers() {
        if (isNull(mowers)) {
            mowers = new TreeSet<>();
        }

        return mowers;
    }
}
