package cv.igrp.platform.process.management.processdefinition.application.queries;

import cv.igrp.framework.core.domain.Query;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetProcessSequenceQuery implements Query {

  @NotBlank(message = "The field <processKey> is required")
  private String processKey;
  @NotBlank(message = "The field <applicationCode> is required")
  private String applicationCode;

}
