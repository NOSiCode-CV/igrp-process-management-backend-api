/* THIS FILE WAS GENERATED AUTOMATICALLY BY iGRP STUDIO. */
/* DO NOT MODIFY IT BECAUSE IT COULD BE REWRITTEN AT ANY TIME. */

package cv.igrp.platform.process.management.processruntime.application.dto;

import cv.igrp.framework.stereotype.IgrpDTO;
import jakarta.validation.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import cv.igrp.platform.process.management.processruntime.application.dto.UserProfileDTO;
import cv.igrp.platform.process.management.shared.application.constants.TaskInstanceStatus;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor


@IgrpDTO
public class TaskInstanceEventListDTO  {

  
  
  private UUID id ;
  
  
  private UUID taskInstanceId ;
  
  
  private String eventType ;
  
  
  private LocalDateTime performedAt ;
  
  
  private String performedBy ;
  
  
  private String obs ;
  
  
  private TaskInstanceStatus status ;
  
  @Valid
  private UserProfileDTO userProfilePerformedBy ;

}