package cv.igrp.platform.process.management.processdefinition.application.queries;

import cv.igrp.framework.core.domain.Query;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetProcessSequenceByIdQuery implements Query {

  @NotBlank(message = "The field <id> is required")
  private String id;

}
