/* THIS FILE WAS GENERATED AUTOMATICALLY BY iGRP STUDIO. */
/* DO NOT MODIFY IT BECAUSE IT COULD BE REWRITTEN AT ANY TIME. */

package cv.igrp.platform.process.management.shared.application.dto;

import cv.igrp.framework.stereotype.IgrpDTO;
import jakarta.validation.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import cv.igrp.platform.process.management.shared.application.constants.TaskAssignmentMode;

@Data
@NoArgsConstructor
@AllArgsConstructor


@IgrpDTO
public class TaskAssignmentRuleDTO  {


  private String taskKey ;


  private String assignee ;


  private String candidateUsers ;


  private String candidateGroups ;


  private TaskAssignmentMode assignmentMode ;


  private Integer priority ;

}
