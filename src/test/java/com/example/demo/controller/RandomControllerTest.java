package com.example.demo.controller;

import com.example.demo.service.RandomNumberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Web-layer test: only the MVC parts of the context are started and the
 * service is replaced by a mock, so the response is fully predictable.
 */
@WebMvcTest(RandomController.class)
class RandomControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private RandomNumberService randomNumberService;

  @Test
  @DisplayName("GET /api/random uses the default 1..100 range")
  void usesDefaultRange() throws Exception {
    given(randomNumberService.between(1, 100)).willReturn(42);

    mockMvc.perform(get("/api/random"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.value").value(42))
      .andExpect(jsonPath("$.min").value(1))
      .andExpect(jsonPath("$.max").value(100));

    then(randomNumberService).should(times(1)).between(1, 100);
  }

  @Test
  @DisplayName("GET /api/random honours min and max query parameters")
  void honoursQueryParameters() throws Exception {
    given(randomNumberService.between(10, 20)).willReturn(13);

    mockMvc.perform(get("/api/random").param("min", "10").param("max", "20"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.value").value(13))
      .andExpect(jsonPath("$.min").value(10))
      .andExpect(jsonPath("$.max").value(20));
  }

  @Test
  @DisplayName("GET /api/random returns 400 when the service rejects the range")
  void returnsBadRequestForInvalidRange() throws Exception {
    given(randomNumberService.between(anyInt(), anyInt()))
      .willThrow(new IllegalArgumentException("min (10) must not be greater than max (1)"));

    mockMvc.perform(get("/api/random").param("min", "10").param("max", "1"))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.error").value("invalid range"))
      .andExpect(jsonPath("$.message").value("min (10) must not be greater than max (1)"));
  }

  @Test
  @DisplayName("GET /api/random returns 400 when a parameter is not a number")
  void returnsBadRequestForNonNumericParameter() throws Exception {
    mockMvc.perform(get("/api/random").param("min", "abc"))
      .andExpect(status().isBadRequest());
  }
}
