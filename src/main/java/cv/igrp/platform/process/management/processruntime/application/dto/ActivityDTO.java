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
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor


@IgrpDTO
public class ActivityDTO  {

  
  
  private String id ;
  
  
  private String name ;
  
  
  private String description ;
  
  
  private String processInstanceId ;
  
  
  private String parentId ;
  
  
  private String parentProcessInstanceId ;
  
  
  private String status ;
  
  
  private String type ;
  
  @Valid
  private List<TaskVariableDTO> variables = new ArrayList<>();

}