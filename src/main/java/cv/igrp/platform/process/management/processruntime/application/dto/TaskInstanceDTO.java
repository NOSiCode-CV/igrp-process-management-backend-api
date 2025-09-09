/* THIS FILE WAS GENERATED AUTOMATICALLY BY iGRP STUDIO. */
/* DO NOT MODIFY IT BECAUSE IT COULD BE REWRITTEN AT ANY TIME. */

package cv.igrp.platform.process.management.processruntime.application.dto;

import cv.igrp.framework.stereotype.IgrpDTO;
import cv.igrp.platform.process.management.shared.application.constants.TaskInstanceStatus;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor


@IgrpDTO
public class TaskInstanceDTO  {



  private UUID id ;


  private String taskKey ;


  private String formKey ;


  private String name ;


  private String externalId ;


  private UUID processInstanceId ;


  private String processKey ;


  private String processNumber ;


  private String businessKey ;


  private String processName ;


  private String applicationBase ;


  private LocalDateTime assignedAt ;


  private String assignedBy ;


  private String searchTerms ;


  private Integer priority ;


  private LocalDateTime startedAt ;


  private String startedBy ;


  private TaskInstanceStatus status ;


  private String statusDesc ;


  private LocalDateTime endedAt ;


  private String endedBy ;

  @Valid
  private List<TaskInstanceEventListDTO> taskInstanceEvents = new ArrayList<>();

}
