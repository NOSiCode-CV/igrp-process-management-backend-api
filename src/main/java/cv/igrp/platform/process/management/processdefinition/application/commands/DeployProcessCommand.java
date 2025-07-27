package cv.igrp.platform.process.management.processdefinition.application.commands;

import cv.igrp.framework.core.domain.Command;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import cv.igrp.platform.process.management.processdefinition.application.dto.ProcessDeploymentRequestDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeployProcessCommand implements Command {

  
  private ProcessDeploymentRequestDTO processdeploymentrequestdto;

}