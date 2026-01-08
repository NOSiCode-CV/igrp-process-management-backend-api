package cv.igrp.platform.process.management.shared.domain.models;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@EqualsAndHashCode
public class Identifier {

  private final UUID value;

  @Builder
  private Identifier(UUID value) {
    this.value = value == null ? UUID.randomUUID() : value;
  }

  public static Identifier create(UUID value) {
    return Identifier.builder()
        .value(value)
        .build();
  }

  public static Identifier create(String value) {
    return Identifier.builder()
        .value(UUID.fromString(value))
        .build();
  }

  public static Identifier generate() {
    return Identifier.builder().build();
  }



}
