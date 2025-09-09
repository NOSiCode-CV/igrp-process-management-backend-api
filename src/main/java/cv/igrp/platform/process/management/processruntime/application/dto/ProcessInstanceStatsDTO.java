/* THIS FILE WAS GENERATED AUTOMATICALLY BY iGRP STUDIO. */
/* DO NOT MODIFY IT BECAUSE IT COULD BE REWRITTEN AT ANY TIME. */

package cv.igrp.platform.process.management.processruntime.application.dto;

import cv.igrp.framework.stereotype.IgrpDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor


@IgrpDTO
public class ProcessInstanceStatsDTO  {



  private Long totalProcessInstances ;


  private Long totalCreatedProcess ;


  private Long totalRunningProcess ;


  private Long totalCompletedProcess ;


  private Long totalSuspendedProcess ;


  private Long totalCanceledProcess ;

}
