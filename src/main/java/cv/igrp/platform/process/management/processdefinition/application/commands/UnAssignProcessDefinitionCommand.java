package cv.igrp.platform.process.management.processdefinition.application.commands;

import cv.igrp.framework.core.domain.Command;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import cv.igrp.platform.process.management.processdefinition.application.dto.AssignProcessDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnAssignProcessDefinitionCommand implements Command {

  
  private AssignProcessDTO assignprocessdto;
  @NotBlank(message = "The field <id> is required")
  private String id;

}