package cv.igrp.platform.process.management.processruntime.application.queries;

import cv.igrp.framework.core.domain.Query;
import jakarta.validation.constraints.*;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListProcessInstancesQuery implements Query {

  @NotBlank(message = "The field <number> is required")
  private String number;
  @NotBlank(message = "The field <procReleaseKey> is required")
  private String procReleaseKey;
  @NotBlank(message = "The field <procReleaseId> is required")
  private String procReleaseId;
  @NotBlank(message = "The field <status> is required")
  private String status;
  @NotBlank(message = "The field <searchTerms> is required")
  private String searchTerms;
  @NotBlank(message = "The field <applicationBase> is required")
  private String applicationBase;
  @NotNull(message = "The field <page> is required")
  private Integer page;
  @NotNull(message = "The field <size> is required")
  private Integer size;

}