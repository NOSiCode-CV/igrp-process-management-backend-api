/* THIS FILE WAS GENERATED AUTOMATICALLY BY iGRP STUDIO. */
/* DO NOT MODIFY IT BECAUSE IT COULD BE REWRITTEN AT ANY TIME. */

package cv.igrp.platform.process.management.processruntime.application.dto;

import cv.igrp.framework.stereotype.IgrpDTO;
import jakarta.validation.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import cv.igrp.platform.process.management.shared.application.constants.ProcessInstanceStatus;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor


@IgrpDTO
public class ProcessInstanceDTO  {

  
  
  private UUID id ;
  
  
  private String procReleaseKey ;
  
  
  private String procReleaseId ;
  
  
  private String number ;
  
  
  private ProcessInstanceStatus status ;
  
  
  private String statusDesc ;
  
  
  private String businessKey ;
  
  
  private String version ;
  
  
  private LocalDateTime startedAt ;
  
  
  private String startedBy ;
  
  
  private LocalDateTime endedAt ;
  
  
  private String endedBy ;
  
  
  private LocalDateTime canceledAt ;
  
  
  private String canceledBy ;
  
  
  private String obsCancel ;

}