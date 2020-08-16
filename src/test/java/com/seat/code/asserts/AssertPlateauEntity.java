package com.seat.code.asserts;

import java.util.UUID;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.mockito.ArgumentCaptor;

import com.seat.code.domain.entity.PlateauEntity;

public class AssertPlateauEntity extends AbstractAssert<AssertPlateauEntity, PlateauEntity> {

    private AssertPlateauEntity(final PlateauEntity actual) {
        super(actual, AssertPlateauEntity.class);
    }

    public static AssertPlateauEntity assertThat(final PlateauEntity actual) {
        return new AssertPlateauEntity(actual);
    }

    public static AssertPlateauEntity assertThat(final ArgumentCaptor<PlateauEntity> argumentCaptor) {
        return assertThat(argumentCaptor.getValue());
    }

    public AssertPlateauEntity hasNullId() {
        Assertions.assertThat(actual.getId()).isNull();
        return this;
    }

    public AssertPlateauEntity hasId(final UUID expectedId) {
        Assertions.assertThat(actual.getId()).isEqualTo(expectedId);
        return this;
    }

    public AssertPlateauEntity hasName(final String expectedName) {
        Assertions.assertThat(actual.getName()).isEqualTo(expectedName);
        return this;
    }

    public AssertPlateauEntity hasLength(final Integer expectedLength) {
        Assertions.assertThat(actual.getLength()).isEqualTo(expectedLength);
        return this;
    }

    public AssertPlateauEntity hasWidth(final Integer expectedWidth) {
        Assertions.assertThat(actual.getWidth()).isEqualTo(expectedWidth);
        return this;
    }

    public AssertPlateauEntity hasNoMowers() {
        Assertions.assertThat(actual.getMowers()).isEmpty();
        return this;
    }
}
