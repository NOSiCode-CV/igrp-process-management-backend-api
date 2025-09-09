package cv.igrp.platform.process.management.shared.domain.models;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString
@EqualsAndHashCode
public final class ResourceName {

  private final String value;

  @Builder
  public ResourceName(String value) {
    this.value = Objects.requireNonNull(value, "Resource name cannot be null");
    if (!this.value.endsWith(".bpmn20.xml")) {
      throw new IllegalArgumentException("Invalid BPMN resource file name");
    }
  }

  public static ResourceName create(String value) {
    return ResourceName.builder()
        .value(value)
        .build();
  }

}
