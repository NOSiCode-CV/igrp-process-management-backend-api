package cv.igrp.platform.process.management.area.application.commands;

import cv.igrp.framework.core.domain.Command;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RemoveProcessDefinitionCommand implements Command {

  @NotBlank(message = "The field <areaId> is required.")
  private String areaId;
  @NotBlank(message = "The field <processDefinitionId> is required.")
  private String processDefinitionId;

}