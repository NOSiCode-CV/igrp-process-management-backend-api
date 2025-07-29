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
public class ListTaskInstancesQuery implements Query {

  @NotBlank(message = "The field <processNumber> is required.")
  private String processNumber;
  @NotBlank(message = "The field <processKey> is required.")
  private String processKey;
  @NotBlank(message = "The field <user> is required.")
  private String user;
  @NotBlank(message = "The field <status> is required.")
  private String status;
  @NotNull(message = "The field <page> is required.")
  private Integer page;
  @NotNull(message = "The field <size> is required.")
  private Integer size;
  @NotBlank(message = "The field <dateFrom> is required.")
  private String dateFrom;
  @NotBlank(message = "The field <dateTo> is required.")
  private String dateTo;

}
