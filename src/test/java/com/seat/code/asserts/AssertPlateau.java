package com.seat.code.asserts;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import org.mockito.ArgumentCaptor;

import com.seat.code.service.model.Plateau;

public class AssertPlateau extends AbstractAssert<AssertPlateau, Plateau> {

    private AssertPlateau(final Plateau actual) {
        super(actual, AssertPlateau.class);
    }

    public static AssertPlateau assertThat(final Plateau actual) {
        return new AssertPlateau(actual);
    }

    public static AssertPlateau assertThat(final ArgumentCaptor<Plateau> argumentCaptor) {
        return assertThat(argumentCaptor.getValue());
    }

    public AssertPlateau hasNullId() {
        Assertions.assertThat(actual.getId()).isNull();
        return this;
    }

    public AssertPlateau hasNullCreationDateTime() {
        Assertions.assertThat(actual.getCreatedDateTime()).isNull();
        return this;
    }

    public AssertPlateau hasName(final String expectedName) {
        Assertions.assertThat(actual.getName()).isEqualTo(expectedName);
        return this;
    }

    public AssertPlateau hasLength(final Integer expectedLength) {
        Assertions.assertThat(actual.getLength()).isEqualTo(expectedLength);
        return this;
    }

    public AssertPlateau hasWidth(final Integer expectedWidth) {
        Assertions.assertThat(actual.getWidth()).isEqualTo(expectedWidth);
        return this;
    }

    public AssertPlateau hasNoMowers() {
        Assertions.assertThat(actual.getMowers()).isEmpty();
        return this;
    }
}
