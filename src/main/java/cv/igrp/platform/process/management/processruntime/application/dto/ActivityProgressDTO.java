/* THIS FILE WAS GENERATED AUTOMATICALLY BY iGRP STUDIO. */
/* DO NOT MODIFY IT BECAUSE IT COULD BE REWRITTEN AT ANY TIME. */

package cv.igrp.platform.process.management.processruntime.application.dto;

import cv.igrp.framework.stereotype.IgrpDTO;
import jakarta.validation.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import cv.igrp.platform.process.management.processruntime.application.dto.TaskVariableDTO;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor


@IgrpDTO
public class ActivityProgressDTO  {

  
  
  private String activityKey ;
  
  
  private String activityName ;
  
  
  private String status ;
  
  
  private String type ;
  
  
  private String processInstanceId ;
  
  
  private String assignee ;
  
  
  private Set<String> candidateUsers  = new HashSet<>();
  
  
  private Set<String> candidateGroups  = new HashSet<>();
  
  
  private LocalDateTime startTime ;
  
  
  private LocalDateTime endTime ;
  
  
  private Long durationMillis ;
  
  
  private String activityId ;
  
  @Valid
  private List<TaskVariableDTO> variables = new ArrayList<>();

}