/* THIS FILE WAS GENERATED AUTOMATICALLY BY iGRP STUDIO. */
/* DO NOT MODIFY IT BECAUSE IT COULD BE REWRITTEN AT ANY TIME. */

package cv.igrp.platform.process.management.area.application.dto;

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
public class AreaRequestDTO  {

  @NotBlank(message = "The field <code> is required")
  
  private String code ;
  @NotBlank(message = "The field <name> is required")
  
  private String name ;
  
  
  private String description ;
  @NotBlank(message = "The field <applicationBase> is required")
  
  private String applicationBase ;
  
  
  private UUID parentId ;
  
  
  private String color ;

}