package cv.igrp.platform.process.management.processruntime.application.commands;

import cv.igrp.framework.core.domain.Command;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import cv.igrp.platform.process.management.processruntime.application.dto.TaskDataDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompleteTaskCommand implements Command {

  
  private TaskDataDTO taskdatadto;
  @NotBlank(message = "The field <id> is required")
  private String id;

}