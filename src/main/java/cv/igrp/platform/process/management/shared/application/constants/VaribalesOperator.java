/* THIS FILE WAS GENERATED AUTOMATICALLY BY iGRP STUDIO. */
/* DO NOT MODIFY IT BECAUSE IT COULD BE REWRITTEN AT ANY TIME. */

package cv.igrp.platform.process.management.shared.application.constants;

import cv.igrp.framework.core.domain.IgrpEnum;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import cv.igrp.platform.process.management.shared.domain.exceptions.IgrpResponseStatusException;


public enum VaribalesOperator implements IgrpEnum<String> {

  EQUALS("EQUALS", "equals"),
  EQUALS_IGNORE_CASE("EQUALS_IGNORE_CASE", "equalsIgnoreCase"),
  NOT_EQUALS("NOT_EQUALS", "notEquals"),
  NOT_EQUALS_IGNORE_CASE("NOT_EQUALS_IGNORE_CASE", "notEqualsIgnoreCase"),
  GREATER_THAN("GREATER_THAN", "greaterThan"),
  GREATER_THAN_OR_EQUAL("GREATER_THAN_OR_EQUAL", "greaterThanOrEqual"),
  LESS_THAN("LESS_THAN", "lessThan"),
  LESS_THAN_OR_EQUAL("LESS_THAN_OR_EQUAL", "lessThanOrEqual"),
  LIKE("LIKE", "like"),
  LIKE_IGNORE_CASE("LIKE_IGNORE_CASE", "likeIgnoreCase");

  private final String code;
  private final String description;

  VaribalesOperator(String code, String description) {
    this.code = code;
    this.description = description;
  }

  @Override
  public String getCode() {
    return code;
  }

  @Override
  public String getDescription() {
    return description;
  }

  /**
   * Pre-built maps for fast lookup.
   */
  private static final Map<String, VaribalesOperator> CODE_MAP = Arrays.stream(values())
      .collect(Collectors.toMap(VaribalesOperator::getCode, Function.identity()));

  /**
   * Attempts to find the enum value associated with the given code.
   *
   * @param code The code to look up
   * @return An Optional containing the enum value if found, empty Optional otherwise
   */
  public static Optional<VaribalesOperator> fromCode(String code) {
    return Optional.ofNullable(CODE_MAP.get(code));
  }

  /**
   * Finds the enum value associated with the given code or throws an exception if not found.
   *
   * @param code The code to look up
   * @return The enum value for the given code
   * @throws IllegalArgumentException if no enum value exists for the given code
   */
  public static VaribalesOperator fromCodeOrThrow(String code) {
    return fromCode(code).orElseThrow(() -> IgrpResponseStatusException.of(HttpStatus.BAD_REQUEST, "Invalid VaribalesOperator for this code: " + code));
  }

  /**
   * Returns a map of code to description.
   */
  public static Map<String, String> codeDescriptionMap() {
    return CODE_MAP.values().stream().collect(Collectors.toMap(VaribalesOperator::getCode, VaribalesOperator::getDescription));
  }

}
