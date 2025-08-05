package cv.igrp.platform.process.management.area.application.queries;

import cv.igrp.framework.core.domain.Query;
import jakarta.validation.constraints.*;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetAreaByIdQuery implements Query {

  @NotBlank(message = "The field <id> is required")
  private String id;

}