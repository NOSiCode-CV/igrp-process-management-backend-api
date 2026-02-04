package cv.igrp.platform.process.management.processdefinition.application.commands;

import cv.igrp.framework.core.domain.Command;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import cv.igrp.platform.process.management.processdefinition.application.dto.ProcessPackageDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImportProcessDefinitionCommand implements Command {

  
  private ProcessPackageDTO processpackagedto;

}