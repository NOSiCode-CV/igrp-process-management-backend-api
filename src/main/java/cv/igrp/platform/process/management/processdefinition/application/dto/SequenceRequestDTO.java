/* THIS FILE WAS GENERATED AUTOMATICALLY BY iGRP STUDIO. */
/* DO NOT MODIFY IT BECAUSE IT COULD BE REWRITTEN AT ANY TIME. */

package cv.igrp.platform.process.management.processdefinition.application.dto;

import cv.igrp.framework.stereotype.IgrpDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor


@IgrpDTO
public class SequenceRequestDTO  {

  @NotBlank(message = "The field <name> is required")

  private String name ;
  @NotBlank(message = "The field <prefix> is required")

  private String prefix ;
  @NotBlank(message = "The field <dateFormat> is required")

  private String dateFormat ;
  @NotNull(message = "The field <checkDigitSize> is required")

  private short checkDigitSize ;
  @NotNull(message = "The field <padding> is required")

  private short padding ;


  private short numberIncrement ;

}
