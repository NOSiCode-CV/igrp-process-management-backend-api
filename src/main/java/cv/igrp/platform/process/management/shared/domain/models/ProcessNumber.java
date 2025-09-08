package cv.igrp.platform.process.management.shared.domain.models;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Objects;

@Getter
@EqualsAndHashCode
public class ProcessNumber {

  private final String value;

  @Builder
  public ProcessNumber(String value) {
    this.value = Objects.requireNonNull(value,"Value cannot be null");
  }

  public static ProcessNumber create(String value) {
    return ProcessNumber.builder().value(value).build();
  }
}
