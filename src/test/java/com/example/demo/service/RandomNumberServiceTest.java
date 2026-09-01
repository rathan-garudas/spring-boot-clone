package com.example.demo.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Plain unit test: no Spring context is started, so this runs in milliseconds.
 */
class RandomNumberServiceTest {

  @Test
  @DisplayName("returns a value inside the requested range")
  void returnsValueInsideRange() {
    RandomNumberService service = new RandomNumberService(new Random(42L));

    for (int i = 0; i < 1_000; i++) {
      assertThat(service.between(1, 100)).isBetween(1, 100);
    }
  }

  @ParameterizedTest
  @CsvSource({"5, 5", "-3, -3", "0, 0"})
  @DisplayName("returns the only possible value when min equals max")
  void returnsSingleValueWhenRangeHasOneElement(int min, int max) {
    RandomNumberService service = new RandomNumberService(new Random());

    assertThat(service.between(min, max)).isEqualTo(min);
  }

  @Test
  @DisplayName("uses the injected Random so results are predictable")
  void usesInjectedRandom() {
    // A Random that always returns 7 from nextInt(bound).
    Random alwaysSeven = new Random() {
      @Override
      public int nextInt(int bound) {
        return 7;
      }
    };
    RandomNumberService service = new RandomNumberService(alwaysSeven);

    assertThat(service.between(10, 100)).isEqualTo(17);
  }

  @Test
  @DisplayName("rejects a range where min is greater than max")
  void rejectsInvertedRange() {
    RandomNumberService service = new RandomNumberService(new Random());

    assertThatThrownBy(() -> service.between(10, 1))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessageContaining("must not be greater than");
  }

  @Test
  @DisplayName("rejects a range wider than an int can hold")
  void rejectsTooWideRange() {
    RandomNumberService service = new RandomNumberService(new Random());

    assertThatThrownBy(() -> service.between(Integer.MIN_VALUE, Integer.MAX_VALUE))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessageContaining("too large");
  }
}
