package com.example.demo.controller;

import com.example.demo.service.RandomNumberService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class RandomController {

  private final RandomNumberService randomNumberService;

  public RandomController(RandomNumberService randomNumberService) {
    this.randomNumberService = randomNumberService;
  }

  @GetMapping("/random")
  public Map<String, Object> random(
    @RequestParam(defaultValue = "1") int min,
    @RequestParam(defaultValue = "100") int max
  ) {
    return Map.of(
      "value", randomNumberService.between(min, max),
      "min", min,
      "max", max
    );
  }

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, String> handleBadRange(IllegalArgumentException ex) {
    return Map.of(
      "error", "invalid range",
      "message", ex.getMessage()
    );
  }
}
