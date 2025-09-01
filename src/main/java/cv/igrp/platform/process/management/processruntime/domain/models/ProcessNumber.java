package cv.igrp.platform.process.management.processruntime.domain.models;

import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
public class ProcessNumber {

  public final String value;

  @Builder
  public ProcessNumber(String value) {
    this.value = Objects.requireNonNull(value,"Value cannot be null");
  }

  public static ProcessNumber create(String value) {
    return ProcessNumber.builder().value(value).build();
  }
}
