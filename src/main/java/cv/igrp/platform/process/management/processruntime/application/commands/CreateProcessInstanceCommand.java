package cv.igrp.platform.process.management.processruntime.application.commands;

import cv.igrp.framework.core.domain.Command;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import cv.igrp.platform.process.management.processruntime.application.dto.CreateProcessRequestDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProcessInstanceCommand implements Command {

  
  private CreateProcessRequestDTO createprocessrequestdto;

}