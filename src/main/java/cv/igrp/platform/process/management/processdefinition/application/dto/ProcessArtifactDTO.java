/* THIS FILE WAS GENERATED AUTOMATICALLY BY iGRP STUDIO. */
/* DO NOT MODIFY IT BECAUSE IT COULD BE REWRITTEN AT ANY TIME. */

package cv.igrp.platform.process.management.processdefinition.application.dto;

import cv.igrp.framework.stereotype.IgrpDTO;
import jakarta.validation.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor


@IgrpDTO
public class ProcessArtifactDTO  {

  
  
  private UUID id ;
  
  
  private String name ;
  
  
  private String key ;
  
  
  private String processDefinitionId ;
  
  
  private String formKey ;
  
  
  private String candidateGroups ;
  
  
  private String dueDate ;
  
  
  private Integer priority ;

}