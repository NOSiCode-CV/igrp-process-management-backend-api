package cv.igrp.platform.process.management.shared.domain.models;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class ProcessNumber {

  private final String value;

  @Builder
  public ProcessNumber(String value) {
    if (value == null || value.trim().isEmpty())
      throw new IllegalArgumentException("The value of the processNumber is required.");
    this.value = value;
  }

  public static ProcessNumber create(String value) {
    return ProcessNumber.builder().value(value).build();
  }
}
