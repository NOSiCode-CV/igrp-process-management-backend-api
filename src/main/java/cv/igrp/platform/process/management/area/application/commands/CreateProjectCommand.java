package cv.igrp.platform.process.management.area.application.commands;

import cv.igrp.framework.core.domain.Command;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import cv.igrp.platform.process.management.area.application.dto.ProjectRequestDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProjectCommand implements Command {

  
  private ProjectRequestDTO projectrequestdto;
  @NotBlank(message = "The field <areaId> is required")
  private String areaId;

}