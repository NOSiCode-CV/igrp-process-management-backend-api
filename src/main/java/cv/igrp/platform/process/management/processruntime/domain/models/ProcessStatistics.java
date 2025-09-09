package cv.igrp.platform.process.management.processruntime.domain.models;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProcessStatistics {
  private final Long totalProcessInstances ;
  private final Long totalCreatedProcess;
  private final Long totalRunningProcess;
  private final Long totalCompletedProcess;
  private final Long totalSuspendedProcess;
  private final Long totalCanceledProcess;

  @Builder
  public ProcessStatistics(Long totalProcessInstances,
                        Long totalCreatedProcess,
                        Long totalRunningProcess,
                        Long totalCompletedProcess,
                        Long totalSuspendedProcess,
                        Long totalCanceledProcess)
  {
    this.totalProcessInstances = totalProcessInstances;
    this.totalCreatedProcess = totalCreatedProcess;
    this.totalRunningProcess = totalRunningProcess;
    this.totalCompletedProcess = totalCompletedProcess;
    this.totalSuspendedProcess = totalSuspendedProcess;
    this.totalCanceledProcess = totalCanceledProcess;
  }
}
