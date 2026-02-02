/* THIS FILE WAS GENERATED AUTOMATICALLY BY iGRP STUDIO. */
/* DO NOT MODIFY IT BECAUSE IT COULD BE REWRITTEN AT ANY TIME. */

package cv.igrp.platform.process.management.processdefinition.application.dto;

import cv.igrp.framework.stereotype.IgrpDTO;
import jakarta.validation.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import cv.igrp.platform.process.management.processdefinition.application.dto.ProcessArtifactDTO;
import cv.igrp.platform.process.management.processdefinition.application.dto.ProcessSequenceDTO;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor


@IgrpDTO
public class ProcessPackageDTO  {

  @NotBlank(message = "The field <processKey> is required")
  
  private String processKey ;
  @NotBlank(message = "The field <processName> is required")
  
  private String processName ;
  @NotBlank(message = "The field <processVersion> is required")
  
  private String processVersion ;
  
  
  private String processDescription ;
  @NotBlank(message = "The field <bpmnXml> is required")
  
  private String bpmnXml ;
  @NotBlank(message = "The field <applicationBase> is required")
  
  private String applicationBase ;
  
  @Valid
  private List<ProcessArtifactDTO> artifacts = new ArrayList<>();
  
  @Valid
  private ProcessSequenceDTO sequence ;
  
  
  private String candidateGroups ;

}