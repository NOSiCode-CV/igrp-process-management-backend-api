package cv.igrp.platform.process.management.processdefinition.application.commands;

import cv.igrp.framework.core.domain.Command;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import cv.igrp.platform.process.management.processdefinition.application.dto.ProcessArtifactRequestDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigureArtifactCommand implements Command {

  
  private ProcessArtifactRequestDTO processartifactrequestdto;
  @NotBlank(message = "The field <id> is required")
  private String id;
  @NotBlank(message = "The field <taskKey> is required")
  private String taskKey;

}