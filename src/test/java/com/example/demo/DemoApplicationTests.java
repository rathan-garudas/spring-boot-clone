package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Starts the whole Spring application context. If a bean is misconfigured,
 * this test is usually the first one to fail.
 */
@SpringBootTest
class DemoApplicationTests {

  @Test
  void contextLoads() {
    // Intentionally empty: the test passes if the context starts without error.
  }
}
