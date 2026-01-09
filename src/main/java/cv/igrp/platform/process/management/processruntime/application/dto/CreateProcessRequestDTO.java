/* THIS FILE WAS GENERATED AUTOMATICALLY BY iGRP STUDIO. */
/* DO NOT MODIFY IT BECAUSE IT COULD BE REWRITTEN AT ANY TIME. */

package cv.igrp.platform.process.management.processruntime.application.dto;

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
public class CreateProcessRequestDTO  {

  
  
  private String processDefinitionId ;
  @NotBlank(message = "The field <processKey> is required")
  
  private String processKey ;
  @NotBlank(message = "The field <applicationBase> is required")
  
  private String applicationBase ;
  
  
  private String businessKey ;
  
  
  private Integer priority ;

}