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

  @NotBlank(message = "The field <applicationBase> is required")
  private String applicationBase;

}