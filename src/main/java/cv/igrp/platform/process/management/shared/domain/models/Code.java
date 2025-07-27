package cv.igrp.platform.process.management.shared.domain.models;

import lombok.Builder;
import lombok.Getter;


@Getter
public class Code {

  private final String value;

  @Builder
  private Code(String value) {
    if (value == null || value.trim().isEmpty())
      throw new IllegalArgumentException("The value of the code is required.");
    this.value = value;
  }

  public static Code create(String value) {
    return Code.builder()
        .value(value)
        .build();
  }

}
