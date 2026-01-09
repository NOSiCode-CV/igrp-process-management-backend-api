package cv.igrp.platform.process.management.processruntime.domain.models;

import cv.igrp.platform.process.management.shared.application.constants.VaribalesOperator;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
public class VariablesExpression {

  private final String name;
  private final VaribalesOperator operator;
  private final Object value;

  @Builder
  public VariablesExpression(String name,
                             VaribalesOperator operator,
                             Object value) {
    this.name = Objects.requireNonNull(name, "Variable name cannot be null");
    this.operator = Objects.requireNonNull(operator, "Operator cannot be null");
    this.value = Objects.requireNonNull(value, "Value cannot be null");
  }

}
