/* THIS FILE WAS GENERATED AUTOMATICALLY BY iGRP STUDIO. */
/* DO NOT MODIFY IT BECAUSE IT COULD BE REWRITTEN AT ANY TIME. */

package cv.igrp.platform.process.management.area.application.dto;

import cv.igrp.framework.stereotype.IgrpDTO;
import jakarta.validation.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import cv.igrp.platform.process.management.shared.application.constants.Status;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor


@IgrpDTO
public class ProcessDefinitionDTO  {

  @NotNull(message = "The field <id> is required")
  
  private UUID id ;
  @NotBlank(message = "The field <processKey> is required")
  
  private String processKey ;
  @NotBlank(message = "The field <releaseId> is required")
  
  private String releaseId ;
  @NotNull(message = "The field <areaId> is required")
  
  private UUID areaId ;
  
  
  private Status status ;
  
  
  private String statusDesc ;
  @NotBlank(message = "The field <version> is required")
  
  private String version ;
  
  
  private LocalDateTime createdAt ;
  
  
  private String createdBy ;
  
  
  private LocalDateTime removedAt ;
  
  
  private String removedBy ;

}