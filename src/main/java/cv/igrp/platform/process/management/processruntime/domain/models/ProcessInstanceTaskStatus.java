package cv.igrp.platform.process.management.processruntime.domain.models;

import cv.igrp.platform.process.management.shared.application.constants.TaskInstanceStatus;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
public class ProcessInstanceTaskStatus {

  private final Code taskKey;
  private final Name taskName;
  private final TaskInstanceStatus status;
  private final Code processInstanceId;

  @Builder
  public ProcessInstanceTaskStatus(Code taskKey,
                                   Name taskName,
                                   TaskInstanceStatus status,
                                   Code processInstanceId) {
    this.taskKey = Objects.requireNonNull(taskKey, "TaskKey cannot be null");
    this.taskName = Objects.requireNonNull(taskName, "TaskName cannot be null");
    this.status = Objects.requireNonNull(status, "Status cannot be null");
    this.processInstanceId = Objects.requireNonNull(processInstanceId, "ProcessInstanceId cannot be null");
  }

}
