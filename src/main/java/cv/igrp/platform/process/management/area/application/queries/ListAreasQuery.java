package cv.igrp.platform.process.management.area.application.queries;

import cv.igrp.framework.core.domain.Query;
import jakarta.validation.constraints.*;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListAreasQuery implements Query {

  @NotBlank(message = "The field <code> is required")
  private String code;
  @NotBlank(message = "The field <name> is required")
  private String name;
  @NotBlank(message = "The field <applicationCode> is required")
  private String applicationCode;

}