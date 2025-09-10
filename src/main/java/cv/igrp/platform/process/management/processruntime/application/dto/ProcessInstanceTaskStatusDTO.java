/* THIS FILE WAS GENERATED AUTOMATICALLY BY iGRP STUDIO. */
/* DO NOT MODIFY IT BECAUSE IT COULD BE REWRITTEN AT ANY TIME. */

package cv.igrp.platform.process.management.processruntime.application.dto;

import cv.igrp.framework.stereotype.IgrpDTO;
import jakarta.validation.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import cv.igrp.platform.process.management.shared.application.constants.TaskInstanceStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor


@IgrpDTO
public class ProcessInstanceTaskStatusDTO  {

  @NotBlank(message = "The field <taskKey> is required")
  
  private String taskKey ;
  
  
  private String name ;
  
  
  private TaskInstanceStatus status ;

}