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
public class TaskPriorityRequestDTO  {

  @NotBlank(message = "The field <code> is required")
  
  private String code ;
  @NotBlank(message = "The field <label> is required")
  
  private String label ;
  @NotNull(message = "The field <weight> is required")
  
  private Integer weight ;
  
  
  private UUID id ;

}