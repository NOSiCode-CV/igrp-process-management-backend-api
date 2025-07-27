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
  @NotBlank(message = "The field <applicationBase> is required")
  private String applicationBase;
  @NotBlank(message = "The field <status> is required")
  private String status;
  @NotBlank(message = "The field <parentId> is required")
  private String parentId;
  @NotNull(message = "The field <page> is required")
  private Integer page;
  @NotNull(message = "The field <size> is required")
  private Integer size;

}