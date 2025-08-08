/* THIS FILE WAS GENERATED AUTOMATICALLY BY iGRP STUDIO. */
/* DO NOT MODIFY IT BECAUSE IT COULD BE REWRITTEN AT ANY TIME. */

package cv.igrp.platform.process.management.processruntime.application.dto;

import cv.igrp.framework.stereotype.IgrpDTO;
import cv.igrp.platform.process.management.shared.application.constants.TaskInstanceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor


@IgrpDTO
public class TaskInstanceListDTO  {



  private UUID id ;


  private String taskKey ;


  private String formKey ;


  private String name ;


  private String processInstanceId ;


  private String processNumber ;


  private String processName ;


  private String assignedBy ;


  private TaskInstanceStatus status ;


  private String statusDesc ;


  private String startedAt ;

}
