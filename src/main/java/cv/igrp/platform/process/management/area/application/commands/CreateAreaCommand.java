package cv.igrp.platform.process.management.area.application.commands;

import cv.igrp.framework.core.domain.Command;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import cv.igrp.platform.process.management.area.application.dto.AreaRequestDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAreaCommand implements Command {

  
  private AreaRequestDTO arearequestdto;

}