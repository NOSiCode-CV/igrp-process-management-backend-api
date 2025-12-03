/* THIS FILE WAS GENERATED AUTOMATICALLY BY iGRP STUDIO. */
/* DO NOT MODIFY IT BECAUSE IT COULD BE REWRITTEN AT ANY TIME. */

package cv.igrp.platform.process.management.processruntime.application.dto;

import cv.igrp.framework.stereotype.IgrpDTO;
import jakarta.validation.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.HashSet;
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

}