package com.seat.code.domain.entity;

import static java.util.Objects.isNull;
import static javax.persistence.CascadeType.ALL;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "plateau")
public class PlateauEntity extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "length", updatable = false, nullable = false)
    private Integer length;

    @Column(name = "width", updatable = false, nullable = false)
    private Integer width;

    @OneToMany(cascade = ALL, mappedBy = "plateau")
    private List<MowerEntity> mowers;

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

    public List<MowerEntity> getMowers() {
        if (isNull(mowers)) {
            mowers = new ArrayList<>();
        }

        return mowers;
    }
}
