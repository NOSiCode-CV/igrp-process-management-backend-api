/* THIS FILE WAS GENERATED AUTOMATICALLY BY iGRP STUDIO. */
/* DO NOT MODIFY IT BECAUSE IT COULD BE REWRITTEN AT ANY TIME. */

package cv.igrp.platform.process.management.processdefinition.application.dto;

import cv.igrp.framework.stereotype.IgrpDTO;
import jakarta.validation.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor


@IgrpDTO
public class ProcessDeploymentDTO  {

  
  
  private UUID id ;
  
  
  private String key ;
  
  
  private String name ;
  
  
  private String description ;
  
  
  private String version ;
  
  
  private String bpmnXml ;
  
  
  private String bpmnUrl ;
  
  
  private String bpmnSourceType ;
  
  
  private String resourceName ;
  
  
  private boolean deployed ;
  
  
  private String deploymentId ;
  
  
  private LocalDateTime deployedAt ;

}