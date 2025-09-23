package cv.igrp.platform.process.management.processruntime.domain.models;

import cv.igrp.platform.process.management.shared.application.constants.TaskEventType;
import cv.igrp.platform.process.management.shared.application.constants.TaskInstanceStatus;
import cv.igrp.platform.process.management.shared.domain.exceptions.IgrpResponseStatusException;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import cv.igrp.platform.process.management.shared.domain.models.ProcessNumber;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

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
  private final List<String> candidateGroups;

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
      List<TaskInstanceEvent> taskInstanceEvents,
      List<String> candidateGroups
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
    this.candidateGroups = candidateGroups == null ? new ArrayList<>() : candidateGroups;
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


  public void claim(TaskOperationData data) {
    if(this.status!=TaskInstanceStatus.CREATED) {
      throw IgrpResponseStatusException.of(HttpStatus.CONFLICT, String.format("Cannot Claim a Task in Status[%s]",this.status));
    }
    this.assignedBy = Objects.requireNonNull(data.getCurrentUser(), "User cannot be null!");
    this.status = TaskInstanceStatus.ASSIGNED;
    this.assignedAt = LocalDateTime.now();
    createTaskInstanceEvent(TaskEventType.CLAIM,data.getCurrentUser(),data.getNote());
  }


  public void assign(TaskOperationData data) {
    if(this.status!=TaskInstanceStatus.CREATED) {
      throw IgrpResponseStatusException.of(HttpStatus.CONFLICT, String.format("Cannot Assign a Task in Status[%s]",this.status));
    }
    this.assignedBy = Objects.requireNonNull(data.getTargetUser(), "Target User cannot be null!");
    this.assignedAt = LocalDateTime.now();
    this.status = TaskInstanceStatus.ASSIGNED;
    if(data.getPriority()!=null && data.getPriority()!=0)
      this.priority = data.getPriority();
    createTaskInstanceEvent(TaskEventType.ASSIGN,data.getCurrentUser(),data.getNote());
  }


  public void unClaim(TaskOperationData data) {
    if(this.status!=TaskInstanceStatus.ASSIGNED) {
      throw IgrpResponseStatusException.of(HttpStatus.CONFLICT, String.format("Cannot UnClaim a Task in Status[%s]",this.status));
    }
    this.assignedAt = null;
    this.assignedBy = null;
    this.status = TaskInstanceStatus.CREATED;
    createTaskInstanceEvent(TaskEventType.UNCLAIM, data.getCurrentUser(), data.getNote());
  }


  public void complete(TaskOperationData data) {
    if(this.status!=TaskInstanceStatus.ASSIGNED) {
      throw IgrpResponseStatusException.of(HttpStatus.CONFLICT, String.format("Cannot Complete a Task in Status[%s]",this.status));
    }
    this.endedBy = Objects.requireNonNull(data.getCurrentUser(), "Current User cannot be null!");
    this.endedAt = LocalDateTime.now();
    this.status = TaskInstanceStatus.COMPLETED;
    createTaskInstanceEvent(TaskEventType.COMPLETE,data.getCurrentUser(),data.getNote());
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
        .candidateGroups(this.candidateGroups)
        .formKey(formKey!=null ? formKey : this.formKey) // if present overrides activity formKey
        .priority(processInstance.getPriority())
        .businessKey(processInstance.getBusinessKey())
        .processInstanceId(processInstance.getId())
        .startedBy(user)
        .build();
  }
}
