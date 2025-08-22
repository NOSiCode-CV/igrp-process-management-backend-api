package cv.igrp.platform.process.management.processruntime.domain.models;

import cv.igrp.platform.process.management.shared.application.constants.TaskEventType;
import cv.igrp.platform.process.management.shared.application.constants.TaskInstanceStatus;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import cv.igrp.platform.process.management.shared.util.TempUtil;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.*;

@Getter
public class TaskInstance {
  private final Identifier id;
  private final Code taskKey;
  private final Code formKey;
  private final Name name;
  private final Code externalId;
  private final Identifier processInstanceId;
  private final Code processNumber;
  private final Code processName;
  private final Code processKey;
  private final Code businessKey;
  private final Code applicationBase;
  private final String searchTerms;
  private TaskInstanceStatus status;
  private LocalDateTime startedAt;
  private Code startedBy;
  private LocalDateTime assignedAt;
  private Code assignedBy;
  private LocalDateTime endedAt;
  private Code endedBy;
  private final Map<String,Object> variables;
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
      Code processNumber,
      Code processKey,
      Code businessKey,
      Code applicationBase,
      String searchTerms,
      TaskInstanceStatus status,
      LocalDateTime startedAt,
      Code startedBy,
      LocalDateTime assignedAt,
      Code assignedBy,
      LocalDateTime endedAt,
      Code endedBy,
      Map<String,Object> taskVariables,
      List<TaskInstanceEvent> taskInstanceEvents
  ) {
      this.id = id == null ? Identifier.generate() : id;
      this.taskKey = Objects.requireNonNull(taskKey, "Task Key Id cannot be null!");
      this.formKey = formKey;
      this.name = Objects.requireNonNull(name, "The Name of the task cannot be null!");
      this.externalId = Objects.requireNonNull(externalId, "External Id cannot be null!");
      this.processInstanceId = processInstanceId;
      this.processNumber = Objects.requireNonNull(processNumber, "ProcessNumber cannot be null!");
      this.processName = processName;
      this.businessKey = businessKey;
      this.applicationBase = applicationBase;
      this.searchTerms = searchTerms;
      this.status = status;
      this.startedAt = startedAt;
      this.startedBy = startedBy;
      this.assignedAt = assignedAt;
      this.assignedBy = assignedBy;
      this.endedAt = endedAt;
      this.endedBy = endedBy;
      this.variables = taskVariables!=null ? taskVariables : new HashMap<>();
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
        throw new IllegalStateException("ProcessInstanceId cannot be null!");
      }
      if(processNumber==null) {
        throw new IllegalStateException("ProcessNumber cannot be null!");
      }
      this.status = TaskInstanceStatus.CREATED;
      if(startedAt==null)
          this.startedAt = LocalDateTime.now();
      this.startedBy = TempUtil.getCurrentUser();//todo remove
      createTaskInstanceEvent(TaskEventType.CREATE,null);
  }


  public void claim(String note) {
      this.status = TaskInstanceStatus.ASSIGNED;
      this.assignedAt = LocalDateTime.now();
      this.assignedBy = TempUtil.getCurrentUser();//todo remove
      createTaskInstanceEvent(TaskEventType.CLAIM,note);
  }


  public void assign(Code user, String note) {
      if(user==null) {
        throw new IllegalStateException("The assigned by (user) cannot be null!");
      }
      this.status = TaskInstanceStatus.ASSIGNED;
      this.assignedAt = LocalDateTime.now();
      this.assignedBy = user;
      createTaskInstanceEvent(TaskEventType.ASSIGN,note);
  }


  public void unClaim(String note) {
      this.status = TaskInstanceStatus.CREATED;
      this.assignedAt = null;
      this.assignedBy = null;
      createTaskInstanceEvent(TaskEventType.UNCLAIM,note);
  }


  public void complete() {
      this.status = TaskInstanceStatus.COMPLETED;
      this.endedAt = LocalDateTime.now();
      this.endedBy = TempUtil.getCurrentUser();//todo remove
      createTaskInstanceEvent(TaskEventType.COMPLETE,null);
  }


  private void createTaskInstanceEvent(TaskEventType eventType, String note) {
      this.taskInstanceEvents.add(
          TaskInstanceEvent.builder()
          .taskInstanceId(Identifier.generate())
          .taskInstanceId(this.id)
          .eventType(eventType)
          .status(this.status)
          .performedAt(LocalDateTime.now())
          .performedBy(TempUtil.getCurrentUser()) //todo remove
          .note(note!=null && !note.isBlank() ? note.trim() : null)
          .build());
  }


  public TaskInstance withIdentity(Code applicationBase,
                                   Code processName,
                                   Code businessKey,
                                   Identifier processInstanceId,
                                   Code formKey
  ) {
      return TaskInstance.builder()
          .id(this.id)
          .taskKey(this.taskKey)
          .externalId(this.externalId)
          .name(this.name)
          .processNumber(this.processNumber)
          .startedAt(this.startedAt)
          .formKey(formKey!=null ? formKey : this.formKey)
          .applicationBase(applicationBase)
          .processName(processName)
          .businessKey(businessKey)
          .processInstanceId(processInstanceId)
          .build();
  }
}
