/* THIS FILE WAS GENERATED AUTOMATICALLY BY iGRP STUDIO. */
/* DO NOT MODIFY IT BECAUSE IT COULD BE REWRITTEN AT ANY TIME. */

package cv.igrp.platform.process.management.processdefinition.application.dto;

import cv.igrp.framework.stereotype.IgrpDTO;
import jakarta.validation.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor


@IgrpDTO
public class ProcessDeploymentListDTO {

  
  
  private String id ;
  
  
  private String processKey ;
  
  
  private String name ;
  
  
  private String description ;
  
  
  private String version ;
  
  
  private String deploymentId ;
  
  
  private String applicationBase ;

}