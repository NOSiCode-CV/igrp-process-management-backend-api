package cv.igrp.platform.process.management.processruntime.application.queries;

import cv.igrp.framework.core.domain.Query;
import jakarta.validation.constraints.*;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListTaskInstancesQuery implements Query {

  @NotBlank(message = "The field <processId> is required")
  private String processId;
  @NotBlank(message = "The field <user> is required")
  private String user;

}