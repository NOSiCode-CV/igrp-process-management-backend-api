package cv.igrp.platform.process.management.processruntime.application.commands;

import cv.igrp.framework.core.domain.Command;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import cv.igrp.platform.process.management.shared.application.dto.ProcessEventDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TriggerProcessEventCommand implements Command {

  
  private ProcessEventDTO processeventdto;

}