package cv.igrp.platform.process.management.processruntime.domain.models;

import cv.igrp.platform.process.management.shared.application.constants.TaskInstanceStatus;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class TaskInstanceEvent {
  private Identifier id;
  private Code taskInstanceId;
  private Code eventType;
  private LocalDateTime startedAt;
  private String startedBy;
  private byte[] inputTask;
  private byte[] outputTask;
  private String startObs;
  private LocalDateTime endedAt;
  private String endedBy;
  private String endObs;
  private TaskInstanceStatus status;


  @Builder
  public TaskInstanceEvent(Identifier id,
                           Code taskInstanceId,
                           Code eventType,
                           LocalDateTime startedAt,
                           String startedBy,
                           byte[] inputTask,
                           byte[] outputTask,
                           String startObs,
                           LocalDateTime endedAt,
                           String endedBy,
                           String endObs,
                           TaskInstanceStatus status) {
    this.id = id == null ? Identifier.generate() : id;
    this.taskInstanceId = Objects.requireNonNull(taskInstanceId, "Task Instance Id cannot be null");
    this.eventType = Objects.requireNonNull(eventType, "Event Type Id cannot be null");
    this.startedAt = startedAt;
    this.startedBy = startedBy;
    this.inputTask = inputTask;
    this.outputTask = outputTask;
    this.startObs = startObs;
    this.endedAt = endedAt;
    this.endedBy = endedBy;
    this.endObs = endObs;
    this.status = status;
  }
}
