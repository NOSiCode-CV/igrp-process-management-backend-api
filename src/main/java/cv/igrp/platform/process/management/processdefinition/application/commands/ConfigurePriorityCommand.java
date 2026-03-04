package cv.igrp.platform.process.management.processdefinition.application.commands;

import cv.igrp.framework.core.domain.Command;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import cv.igrp.platform.process.management.processdefinition.application.dto.TaskPriorityRequestDTO;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurePriorityCommand implements Command {

  
  private List<TaskPriorityRequestDTO> taskpriorityrequestdto;
  @NotBlank(message = "The field <processKey> is required")
  private String processKey;

}