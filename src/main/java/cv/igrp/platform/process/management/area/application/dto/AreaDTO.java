/* THIS FILE WAS GENERATED AUTOMATICALLY BY iGRP STUDIO. */
/* DO NOT MODIFY IT BECAUSE IT COULD BE REWRITTEN AT ANY TIME. */

package cv.igrp.platform.process.management.area.application.dto;

import cv.igrp.framework.stereotype.IgrpDTO;
import jakarta.validation.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import cv.igrp.platform.process.management.area.application.dto.ProjectDTO;
import cv.igrp.platform.process.management.shared.application.constants.Status;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor


@IgrpDTO
public class AreaDTO  {

  
  
  private UUID id ;
  
  
  private UUID parentId ;
  
  @Valid
  private List<ProjectDTO> projects = new ArrayList<>();
  
  
  private String code ;
  
  
  private String name ;
  
  
  private String description ;
  
  
  private Status status ;
  
  
  private String applicationCode ;

}