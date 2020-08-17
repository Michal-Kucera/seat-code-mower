package com.seat.code.asserts;

import java.util.UUID;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import com.seat.code.controller.model.RectangularPlateauDetail;

public class AssertRectangularPlateauDetail extends AbstractAssert<AssertRectangularPlateauDetail, RectangularPlateauDetail> {

    private AssertRectangularPlateauDetail(final RectangularPlateauDetail actual) {
        super(actual, AssertRectangularPlateauDetail.class);
    }

    public static AssertRectangularPlateauDetail assertThat(final RectangularPlateauDetail actual) {
        return new AssertRectangularPlateauDetail(actual);
    }

    public AssertRectangularPlateauDetail hasName(final String expectedName) {
        Assertions.assertThat(actual.getName()).isEqualTo(expectedName);
        return this;
    }

    public AssertRectangularPlateauDetail hasLength(final Integer expectedLength) {
        Assertions.assertThat(actual.getSize()).isNotNull();
        Assertions.assertThat(actual.getSize().getLength()).isEqualTo(expectedLength);
        return this;
    }

    public AssertRectangularPlateauDetail hasWidth(final Integer expectedWidth) {
        Assertions.assertThat(actual.getSize()).isNotNull();
        Assertions.assertThat(actual.getSize().getWidth()).isEqualTo(expectedWidth);
        return this;
    }

    public AssertRectangularPlateauDetail hasMowersSize(final int expectedMowersSize) {
        Assertions.assertThat(actual.getMowers()).hasSize(expectedMowersSize);
        return this;
    }

    public AssertRectangularPlateauDetail hasMowers(final UUID expectedMower) {
        Assertions.assertThat(actual.getMowers()).contains(expectedMower);
        return this;
    }
}
