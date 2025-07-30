package cv.igrp.platform.process.management.area.application.commands;

import cv.igrp.framework.core.domain.Command;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import cv.igrp.platform.process.management.area.application.dto.ProcessDefinitionRequestDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProcessDefinitionCommand implements Command {

  
  private ProcessDefinitionRequestDTO processdefinitionrequestdto;
  @NotBlank(message = "The field <areaId> is required.")
  private String areaId;

}