package cv.igrp.platform.process.management.processruntime.application.queries;

import cv.igrp.framework.core.domain.Query;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetTaskHistoryQuery implements Query {

  @NotNull(message = "The field <page> is required.")
  private Integer page;
  @NotNull(message = "The field <size> is required.")
  private Integer size;
  @NotBlank(message = "The field <id> is required.")
  private String id;

}
