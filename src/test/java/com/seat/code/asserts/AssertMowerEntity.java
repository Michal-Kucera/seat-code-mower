package com.seat.code.asserts;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.mockito.ArgumentCaptor;

import com.seat.code.domain.entity.MowerEntity;
import com.seat.code.domain.entity.MowerEntityOrientation;
import com.seat.code.domain.entity.PlateauEntity;

public class AssertMowerEntity extends AbstractAssert<AssertMowerEntity, MowerEntity> {

    private AssertMowerEntity(final MowerEntity actual) {
        super(actual, AssertMowerEntity.class);
    }

    public static AssertMowerEntity assertThat(final MowerEntity actual) {
        return new AssertMowerEntity(actual);
    }

    public static AssertMowerEntity assertThat(final ArgumentCaptor<MowerEntity> argumentCaptor) {
        return assertThat(argumentCaptor.getValue());
    }

    public AssertMowerEntity hasNullId() {
        Assertions.assertThat(actual.getId()).isNull();
        return this;
    }

    public AssertMowerEntity hasNullVersion() {
        Assertions.assertThat(actual.getVersion()).isNull();
        return this;
    }

    public AssertMowerEntity hasName(final String expectedName) {
        Assertions.assertThat(actual.getName()).isEqualTo(expectedName);
        return this;
    }

    public AssertMowerEntity hasPlateau(final PlateauEntity expectedPlateauEntity) {
        Assertions.assertThat(actual.getPlateau()).isEqualTo(expectedPlateauEntity);
        return this;
    }

    public AssertMowerEntity hasLatitude(final Integer expectedLatitude) {
        Assertions.assertThat(actual.getLatitude()).isEqualTo(expectedLatitude);
        return this;
    }

    public AssertMowerEntity hasLongitude(final Integer expectedLongitude) {
        Assertions.assertThat(actual.getLongitude()).isEqualTo(expectedLongitude);
        return this;
    }

    public AssertMowerEntity hasOrientation(final MowerEntityOrientation expectedOrientation) {
        Assertions.assertThat(actual.getOrientation()).isEqualTo(expectedOrientation);
        return this;
    }
}
