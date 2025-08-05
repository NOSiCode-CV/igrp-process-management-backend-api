package cv.igrp.platform.process.management.area.application.queries;

import cv.igrp.framework.core.domain.Query;
import jakarta.validation.constraints.*;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListProcessDefinitionsQuery implements Query {

  @NotBlank(message = "The field <processKey> is required")
  private String processKey;
  @NotBlank(message = "The field <status> is required")
  private String status;
  @NotBlank(message = "The field <releaseId> is required")
  private String releaseId;
  @NotNull(message = "The field <page> is required")
  private Integer page;
  @NotNull(message = "The field <size> is required")
  private Integer size;
  @NotBlank(message = "The field <areaId> is required")
  private String areaId;

}