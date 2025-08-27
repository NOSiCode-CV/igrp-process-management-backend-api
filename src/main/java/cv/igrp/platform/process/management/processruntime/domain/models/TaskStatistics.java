package cv.igrp.platform.process.management.processruntime.domain.models;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TaskStatistics {
  private final Long totalTaskInstances;
  private final Long totalAvailableTasks;
  private final Long totalAssignedTasks;
  private final Long totalSuspendedTasks;
  private final Long totalCompletedTasks;
  private final Long totalCanceledTasks;

  @Builder
  public TaskStatistics(Long totalTaskInstances,
                        Long totalAvailableTasks,
                        Long totalAssignedTasks,
                        Long totalCompletedTasks,
                        Long totalSuspendedTasks,
                        Long totalCanceledTasks)
  {
    this.totalTaskInstances = totalTaskInstances;
    this.totalAvailableTasks = totalAvailableTasks;
    this.totalAssignedTasks = totalAssignedTasks;
    this.totalCompletedTasks = totalCompletedTasks;
    this.totalSuspendedTasks = totalSuspendedTasks;
    this.totalCanceledTasks = totalCanceledTasks;
  }
}
