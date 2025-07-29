package cv.igrp.platform.process.management.processruntime.domain.models;

import cv.igrp.platform.process.management.shared.application.constants.TaskInstanceStatus;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class TaskInstance {
  private final Identifier id;
  private final Code processInstanceId;
  private final Code processType;
  private final Code processNumber;
  private final Code taskKey;
  private final Code taskKeyDesc;
  private final Code externalId;
  private final Code applicationBase;
  private final Code user;
  private final String searchTerms;
  private final LocalDateTime startedAt;
  private final String startedBy;
  private final TaskInstanceStatus status;
  private final List<TaskInstanceEvent> taskInstanceEvents;

  @Builder
  public TaskInstance(Identifier id,
                      Code processInstanceId,
                      Code processType,
                      Code processNumber,
                      Code taskKey,
                      Code taskKeyDesc,
                      Code externalId,
                      Code applicationBase,
                      Code user,
                      String searchTerms,
                      LocalDateTime startedAt,
                      String startedBy,
                      TaskInstanceStatus status,
                      List<TaskInstanceEvent> taskInstanceEvents) {
    this.id = id == null ? Identifier.generate() : id;
    this.processInstanceId = processInstanceId;
    this.processType = processType;
    this.processNumber = processNumber;
    this.taskKey = taskKey;
    this.taskKeyDesc = taskKeyDesc;
    this.externalId = externalId;
    this.applicationBase = applicationBase;
    this.user = user;
    this.searchTerms = searchTerms;
    this.startedAt = startedAt;
    this.startedBy = startedBy;
    this.status = status == null ? TaskInstanceStatus.CREATED : status;
    this.taskInstanceEvents = taskInstanceEvents!=null ? taskInstanceEvents : new ArrayList<>();
  }

}
