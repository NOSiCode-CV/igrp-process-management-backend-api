package cv.igrp.platform.process.management.processruntime.application.queries;

import cv.igrp.framework.core.domain.Query;
import jakarta.validation.constraints.*;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetActivityProgressQuery implements Query {

  @NotBlank(message = "The field <processIdentifier> is required")
  private String processIdentifier;
  @NotBlank(message = "The field <type> is required")
  private String type;

}