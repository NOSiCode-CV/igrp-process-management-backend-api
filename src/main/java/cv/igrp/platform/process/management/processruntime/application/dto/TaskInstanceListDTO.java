/* THIS FILE WAS GENERATED AUTOMATICALLY BY iGRP STUDIO. */
/* DO NOT MODIFY IT BECAUSE IT COULD BE REWRITTEN AT ANY TIME. */

package cv.igrp.platform.process.management.processruntime.application.dto;

import cv.igrp.framework.stereotype.IgrpDTO;
import cv.igrp.platform.process.management.shared.application.constants.TaskInstanceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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


  private String candidateGroups ;


  private String processInstanceId ;


  private String processNumber ;


  private String processName ;


  private String processKey ;


  private String businessKey ;


  private Integer priority ;


  private String assignedBy ;


  private LocalDateTime assignedAt ;


  private String startedBy ;


  private LocalDateTime startedAt ;


  private String endedBy ;


  private LocalDateTime endedAt ;


  private TaskInstanceStatus status ;


  private String statusDesc ;

}
