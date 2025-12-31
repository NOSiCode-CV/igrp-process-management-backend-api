package cv.igrp.platform.process.management.processruntime.application.commands;

import cv.igrp.framework.core.domain.Command;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import cv.igrp.platform.process.management.processruntime.application.dto.VariablesFilterDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListProcessInstancesCommand implements Command {

  
  private VariablesFilterDTO variablesfilterdto;
  @NotBlank(message = "The field <number> is required")
  private String number;
  @NotBlank(message = "The field <procReleaseKey> is required")
  private String procReleaseKey;
  @NotBlank(message = "The field <procReleaseId> is required")
  private String procReleaseId;
  @NotBlank(message = "The field <status> is required")
  private String status;
  @NotBlank(message = "The field <applicationBase> is required")
  private String applicationBase;
  @NotBlank(message = "The field <dateFrom> is required")
  private String dateFrom;
  @NotBlank(message = "The field <dateTo> is required")
  private String dateTo;
  @NotNull(message = "The field <page> is required")
  private Integer page;
  @NotNull(message = "The field <size> is required")
  private Integer size;

}