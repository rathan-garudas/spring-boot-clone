package com.example.demo.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HelloController.class)
class HelloControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @DisplayName("GET /api/hello returns the greeting payload")
  void returnsGreeting() throws Exception {
    mockMvc.perform(get("/api/hello"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.message").value("Hello, world!"))
      .andExpect(jsonPath("$.status").value("ok"));
  }

  @Test
  @DisplayName("an unmapped path returns 404")
  void unknownPathReturnsNotFound() throws Exception {
    mockMvc.perform(get("/api/does-not-exist"))
      .andExpect(status().isNotFound());
  }
}
