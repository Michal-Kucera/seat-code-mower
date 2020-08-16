package com.seat.code.asserts;

import java.util.UUID;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.mockito.ArgumentCaptor;

import com.seat.code.service.model.Mower;
import com.seat.code.service.model.MowerOrientation;

public class AssertMower extends AbstractAssert<AssertMower, Mower> {

    private AssertMower(final Mower actual) {
        super(actual, AssertMower.class);
    }

    public static AssertMower assertThat(final Mower actual) {
        return new AssertMower(actual);
    }

    public static AssertMower assertThat(final ArgumentCaptor<Mower> argumentCaptor) {
        return assertThat(argumentCaptor.getValue());
    }

    public AssertMower hasNullId() {
        Assertions.assertThat(actual.getId()).isNull();
        return this;
    }

    public AssertMower hasId(final UUID expectedMowerId) {
        Assertions.assertThat(actual.getId()).isEqualTo(expectedMowerId);
        return this;
    }

    public AssertMower hasName(final String expectedName) {
        Assertions.assertThat(actual.getName()).isEqualTo(expectedName);
        return this;
    }

    public AssertMower hasPlateauId(final UUID expectedPlateauId) {
        Assertions.assertThat(actual.getPlateauId()).isEqualTo(expectedPlateauId);
        return this;
    }

    public AssertMower hasLatitude(final Integer expectedLatitude) {
        Assertions.assertThat(actual.getLatitude()).isEqualTo(expectedLatitude);
        return this;
    }

    public AssertMower hasLongitude(final Integer expectedLongitude) {
        Assertions.assertThat(actual.getLongitude()).isEqualTo(expectedLongitude);
        return this;
    }

    public AssertMower hasOrientation(final MowerOrientation expectedOrientation) {
        Assertions.assertThat(actual.getOrientation()).isEqualTo(expectedOrientation);
        return this;
    }
}
