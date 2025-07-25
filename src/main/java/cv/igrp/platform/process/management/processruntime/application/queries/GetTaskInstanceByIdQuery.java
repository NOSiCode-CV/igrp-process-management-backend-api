package cv.igrp.platform.process.management.processruntime.application.queries;

import cv.igrp.framework.core.domain.Query;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetTaskInstanceByIdQuery implements Query {

  @NotBlank(message = "The field <id> is required.")
  private String id;

}
