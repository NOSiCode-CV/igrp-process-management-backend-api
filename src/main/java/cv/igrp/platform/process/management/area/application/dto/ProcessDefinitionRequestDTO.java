/* THIS FILE WAS GENERATED AUTOMATICALLY BY iGRP STUDIO. */
/* DO NOT MODIFY IT BECAUSE IT COULD BE REWRITTEN AT ANY TIME. */

package cv.igrp.platform.process.management.area.application.dto;

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
public class ProcessDefinitionRequestDTO  {

  @NotBlank(message = "The field <processKey> is required")
  
  private String processKey ;
  @NotBlank(message = "The field <releaseId> is required")
  
  private String releaseId ;
  
  
  private String version ;
  @NotBlank(message = "The field <name> is required")
  
  private String name ;

}