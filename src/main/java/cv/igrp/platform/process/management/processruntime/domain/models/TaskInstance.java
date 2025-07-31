package cv.igrp.platform.process.management.processruntime.domain.models;

import cv.igrp.platform.process.management.shared.application.constants.TaskInstanceStatus;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class TaskInstance {
  private final Identifier id;
  private final Identifier processInstanceId;
  private final Code processType;
  private final Code processNumber;
  private final Code taskKey;
  private final Name name;
  private final Code externalId;
  private final Code applicationBase;
  private final Code user;
  private final Code searchTerms;
  private final LocalDateTime startedAt;
  private final String startedBy;
  private final TaskInstanceStatus status;
  private final Code reason;
  private final Map<String,Object> taskVariables;
  private final List<TaskInstanceEvent> taskInstanceEvents;

  @Builder
  public TaskInstance(Identifier id,
                      Identifier processInstanceId,
                      Code processType,
                      Code processNumber,
                      Code taskKey,
                      Name name,
                      Code externalId,
                      Code applicationBase,
                      Code user,
                      Code searchTerms,
                      LocalDateTime startedAt,
                      String startedBy,
                      TaskInstanceStatus status,
                      Code reason,
                      Map<String,Object> taskVariables,
                      List<TaskInstanceEvent> taskInstanceEvents) {
    this.id = id == null ? Identifier.generate() : id;
    this.processInstanceId = processInstanceId;
    this.processType = processType;
    this.processNumber = processNumber;
    this.taskKey = taskKey;
    this.name = name;
    this.externalId = externalId;
    this.applicationBase = applicationBase;
    this.user = user;
    this.searchTerms = searchTerms;
    this.startedAt = startedAt;
    this.startedBy = startedBy;
    this.status = status == null ? TaskInstanceStatus.CREATED : status;
    this.reason = reason;
    this.taskVariables = taskVariables!=null ? taskVariables : Map.of();
    this.taskInstanceEvents = taskInstanceEvents!=null ? taskInstanceEvents : new ArrayList<>();
  }

}
