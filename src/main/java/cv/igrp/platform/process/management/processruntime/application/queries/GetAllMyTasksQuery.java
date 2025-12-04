package cv.igrp.platform.process.management.processruntime.application.queries;

import cv.igrp.framework.core.domain.Query;
import jakarta.validation.constraints.*;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetAllMyTasksQuery implements Query {

  @NotBlank(message = "The field <processInstanceId> is required")
  private String processInstanceId;
  @NotBlank(message = "The field <processNumber> is required")
  private String processNumber;
  @NotBlank(message = "The field <applicationBase> is required")
  private String applicationBase;
  @NotBlank(message = "The field <processName> is required")
  private String processName;
  @NotBlank(message = "The field <candidateGroups> is required")
  private String candidateGroups;
  @NotBlank(message = "The field <status> is required")
  private String status;
  @NotBlank(message = "The field <dateFrom> is required")
  private String dateFrom;
  @NotBlank(message = "The field <dateTo> is required")
  private String dateTo;
  @NotNull(message = "The field <page> is required")
  private Integer page;
  @NotNull(message = "The field <size> is required")
  private Integer size;

}