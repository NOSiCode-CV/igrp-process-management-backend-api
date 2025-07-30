package cv.igrp.platform.process.management.processdefinition.application.queries;

import cv.igrp.framework.core.domain.Query;
import jakarta.validation.constraints.*;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListDeploymentsQuery implements Query {

  @NotBlank(message = "The field <applicationBase> is required.")
  private String applicationBase;
  @NotBlank(message = "The field <processName> is required.")
  private String processName;
  @NotBlank(message = "The field <page> is required.")
  private String page;
  @NotBlank(message = "The field <size> is required.")
  private String size;

}