package com.seat.code.domain.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;

@MappedSuperclass
class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @CreatedDate
    @Column(name = "created_date_time", updatable = false, nullable = false)
    private LocalDateTime createdDateTime;

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }
}
