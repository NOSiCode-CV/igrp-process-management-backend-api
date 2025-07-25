/* THIS FILE WAS GENERATED AUTOMATICALLY BY iGRP STUDIO. */
/* DO NOT MODIFY IT BECAUSE IT COULD BE REWRITTEN AT ANY TIME. */

package cv.igrp.platform.process.management.area.application.dto;

import cv.igrp.framework.stereotype.IgrpDTO;
import jakarta.validation.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import cv.igrp.platform.process.management.shared.application.constants.Status;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor


@IgrpDTO
public class ProjectDTO  {

  
  
  private UUID id ;
  
  
  private String code ;
  
  
  private String name ;
  
  
  private UUID areaId ;
  
  
  private Status status ;

}