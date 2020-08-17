package com.seat.code.asserts;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import com.seat.code.controller.model.Mower;
import com.seat.code.controller.model.MowerOrientation;

public class AssertMowerResponse extends AbstractAssert<AssertMowerResponse, Mower> {

    private AssertMowerResponse(final Mower actual) {
        super(actual, AssertMowerResponse.class);
    }

    public static AssertMowerResponse assertThat(final Mower actual) {
        return new AssertMowerResponse(actual);
    }

    public AssertMowerResponse hasName(final String expectedName) {
        Assertions.assertThat(actual.getName()).isEqualTo(expectedName);
        return this;
    }

    public AssertMowerResponse hasLatitude(final Integer expectedLatitude) {
        Assertions.assertThat(actual.getPosition().getLatitude()).isEqualTo(expectedLatitude);
        return this;
    }

    public AssertMowerResponse hasLongitude(final Integer expectedLongitude) {
        Assertions.assertThat(actual.getPosition().getLongitude()).isEqualTo(expectedLongitude);
        return this;
    }

    public AssertMowerResponse hasOrientation(final MowerOrientation expectedOrientation) {
        Assertions.assertThat(actual.getPosition().getOrientation()).isEqualTo(expectedOrientation);
        return this;
    }
}
