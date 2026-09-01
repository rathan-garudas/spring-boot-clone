package com.example.demo.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RandomNumberService {

  private final Random random;

  public RandomNumberService() {
    this(new Random());
  }

  // Package-private so tests can inject a predictable Random.
  RandomNumberService(Random random) {
    this.random = random;
  }

  public int between(int min, int max) {
    if (min > max) {
      throw new IllegalArgumentException("min (" + min + ") must not be greater than max (" + max + ")");
    }
    long span = (long) max - (long) min + 1L;
    if (span > Integer.MAX_VALUE) {
      throw new IllegalArgumentException("range between min and max is too large");
    }
    return min + random.nextInt((int) span);
  }
}
