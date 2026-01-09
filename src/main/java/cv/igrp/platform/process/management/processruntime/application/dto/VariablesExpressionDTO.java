/* THIS FILE WAS GENERATED AUTOMATICALLY BY iGRP STUDIO. */
/* DO NOT MODIFY IT BECAUSE IT COULD BE REWRITTEN AT ANY TIME. */

package cv.igrp.platform.process.management.processruntime.application.dto;

import cv.igrp.framework.stereotype.IgrpDTO;
import jakarta.validation.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import cv.igrp.platform.process.management.shared.application.constants.VaribalesOperator;

@Data
@NoArgsConstructor
@AllArgsConstructor


@IgrpDTO
public class VariablesExpressionDTO  {

  @NotBlank(message = "The field <name> is required")
  
  private String name ;
  @NotNull(message = "The field <operator> is required")
  
  private VaribalesOperator operator ;
  @NotNull(message = "The field <value> is required")
  
  private Object value ;

}