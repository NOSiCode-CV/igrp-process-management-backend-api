package cv.igrp.platform.process.management.processruntime.domain.models;

import cv.igrp.platform.process.management.shared.application.constants.TaskEventType;
import cv.igrp.platform.process.management.shared.application.constants.TaskInstanceStatus;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import cv.igrp.platform.process.management.shared.domain.models.ProcessNumber;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class TaskInstance {
  private final Identifier id;
  private final Code taskKey;
  private final Code formKey;
  private final Name name;
  private final Code externalId;
  private final Identifier processInstanceId;
  private final ProcessNumber processNumber;
  private final Code processName;
  private final Code processKey;
  private final Code businessKey;
  private final Code applicationBase;
  private final String searchTerms;
  private Integer priority;
  private TaskInstanceStatus status;
  private LocalDateTime startedAt;
  private final Code startedBy;
  private LocalDateTime assignedAt;
  private Code assignedBy;
  private LocalDateTime endedAt;
  private Code endedBy;
  private final List<TaskInstanceEvent> taskInstanceEvents;

  @Builder
  public TaskInstance(
      Identifier id,
      Code taskKey,
      Code formKey,
      Name name,
      Code externalId,
      Identifier processInstanceId,
      Code processName,
      ProcessNumber processNumber,
      Code processKey,
      Code businessKey,
      Code applicationBase,
      String searchTerms,
      Integer priority,
      TaskInstanceStatus status,
      LocalDateTime startedAt,
      Code startedBy,
      LocalDateTime assignedAt,
      Code assignedBy,
      LocalDateTime endedAt,
      Code endedBy,
      List<TaskInstanceEvent> taskInstanceEvents
  ) {
    this.id = id == null ? Identifier.generate() : id;
    this.taskKey = Objects.requireNonNull(taskKey, "Task Key cannot be null!");
    this.formKey = formKey;
    this.name = Objects.requireNonNull(name, "The Name of the task cannot be null!");
    this.externalId = Objects.requireNonNull(externalId, "External Id cannot be null!");
    this.processInstanceId = processInstanceId;
    this.processNumber = processNumber;
    this.processName = processName;
    this.businessKey = businessKey;
    this.applicationBase = applicationBase;
    this.searchTerms = searchTerms;
    this.priority = priority;
    this.status = status;
    this.startedAt = startedAt;
    this.startedBy = startedBy;
    this.assignedAt = assignedAt;
    this.assignedBy = assignedBy;
    this.endedAt = endedAt;
    this.endedBy = endedBy;
    this.taskInstanceEvents = taskInstanceEvents != null ? taskInstanceEvents : new ArrayList<>();
    this.processKey = processKey;
  }


  public void create() {
    if(applicationBase==null) {
      throw new IllegalStateException("ApplicationBase cannot be null!");
    }
    if(processName ==null) {
      throw new IllegalStateException("Process Name cannot be null!");
    }
    if(processInstanceId==null) {
      throw new IllegalStateException("Process Instance Id cannot be null!");
    }
    if(processNumber==null) {
      throw new IllegalStateException("ProcessNumber cannot be null!");
    }
    if(startedBy==null) {
      throw new IllegalStateException("User cannot be null!");
    }
    this.status = TaskInstanceStatus.CREATED;
    if(startedAt==null)
        this.startedAt = LocalDateTime.now();
    createTaskInstanceEvent(TaskEventType.CREATE,this.startedBy,null);
  }


  public void claim(Code user, String note) {
    this.assignedBy = Objects.requireNonNull(user, "User cannot be null!");
    this.status = TaskInstanceStatus.ASSIGNED;
    this.assignedAt = LocalDateTime.now();
    createTaskInstanceEvent(TaskEventType.CLAIM,user,note);
  }


  public void assign(Code user, Code targetUser, Integer priority, String note) {
    this.assignedBy = Objects.requireNonNull(targetUser, "Target User cannot be null!");
    this.status = TaskInstanceStatus.ASSIGNED;
    this.assignedAt = LocalDateTime.now();
    if(priority!=null)
      this.priority = priority;
    createTaskInstanceEvent(TaskEventType.ASSIGN,user,note);
  }


  public void unClaim(Code user, String note) {
    this.status = TaskInstanceStatus.CREATED;
    this.assignedAt = null;
    this.assignedBy = null;
    createTaskInstanceEvent(TaskEventType.UNCLAIM, user, note);
  }


  public void complete(Code user) {
    this.endedBy = Objects.requireNonNull(user, "Current User cannot be null!");
    this.endedAt = LocalDateTime.now();
    this.status = TaskInstanceStatus.COMPLETED;
    createTaskInstanceEvent(TaskEventType.COMPLETE,user,null);
  }


  private void createTaskInstanceEvent(TaskEventType eventType, Code user, String note) {
    this.taskInstanceEvents.add(
        TaskInstanceEvent.builder()
        .taskInstanceId(Identifier.generate())
        .taskInstanceId(this.id)
        .eventType(eventType)
        .status(this.status)
        .performedBy(user)
        .note(note!=null && !note.isBlank() ? note.trim() : null)
        .build());
  }


  public TaskInstance withProperties(ProcessInstance processInstance, Code formKey, Code user) {
    return TaskInstance.builder()
        .id(this.id)
        .taskKey(this.taskKey)
        .externalId(this.externalId)
        .name(this.name)
        .startedAt(this.startedAt)
        .formKey(formKey!=null ? formKey : this.formKey) // if present overrides activity formKey
        .priority(processInstance.getPriority())
        .applicationBase(processInstance.getApplicationBase())
        .processNumber(processInstance.getNumber())
        .processName(Code.create(processInstance.getName()))
        .businessKey(processInstance.getBusinessKey())
        .processInstanceId(processInstance.getId())
        .startedBy(user)
        .build();
  }
}
