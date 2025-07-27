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
public class ProcessDeploymentRequestDTO  {

  
  
  private String name ;
  
  
  private String description ;
  @NotBlank(message = "The field <key> is required")
  
  private String key ;
  @NotBlank(message = "The field <resourceName> is required")
  
  private String resourceName ;
  @NotBlank(message = "The field <bpmnXml> is required")
  
  private String bpmnXml ;

}