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
public class ProcessArtifactRequestDTO  {

  @NotBlank(message = "The field <name> is required")
  
  private String name ;
  @NotBlank(message = "The field <key> is required")
  
  private String key ;
  @NotBlank(message = "The field <formKey> is required")
  
  private String formKey ;
  
  
  private String candidateGroups ;

}