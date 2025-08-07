package cv.igrp.platform.process.management.processruntime.domain.models;

import cv.igrp.platform.process.management.shared.application.constants.TaskEventType;
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
import java.util.Objects;

@Getter
public class TaskInstance {
  private final Code processNumber;
  private final Code externalId;
  private final Code taskKey;
  private final Name name;
  private final Identifier id;
  private final Identifier processInstanceId;
  private final Code processType;
  private final Code applicationBase;
  private TaskInstanceStatus status;
  private LocalDateTime startedAt;
  private Code startedBy;
  private LocalDateTime assignedAt;
  private Code assignedBy;
  private LocalDateTime endedAt;
  private Code endedBy;
  private final String searchTerms;
  private final Map<String,Object> variables;
  private final List<TaskInstanceEvent> taskInstanceEvents;

    @Builder
    public TaskInstance(
        Code processNumber,
        Code externalId,
        Code taskKey,
        Name name,
        Identifier id,
        Identifier processInstanceId,
        Code processType,
        Code applicationBase,
        TaskInstanceStatus status,
        LocalDateTime startedAt,
        Code startedBy,
        LocalDateTime assignedAt,
        Code assignedBy,
        LocalDateTime endedAt,
        Code endedBy,
        String searchTerms,
        Map<String,Object> taskVariables,
        List<TaskInstanceEvent> taskInstanceEvents)
    {
      this.processNumber = Objects.requireNonNull(processNumber, "ProcessNumber cannot be null!");
      this.taskKey = Objects.requireNonNull(taskKey, "Task Key Id cannot be null!");
      this.externalId = Objects.requireNonNull(externalId, "External Id cannot be null!");
      this.name = Objects.requireNonNull(name, "The Name of the task cannot be null!");
      this.id = id == null ? Identifier.generate() : id;
      this.processInstanceId = processInstanceId;
      this.processType = processType;
      this.applicationBase = applicationBase;
      this.status = status;
      this.startedAt = startedAt;
      this.startedBy = startedBy;
      this.assignedAt = assignedAt;
      this.assignedBy = assignedBy;
      this.endedAt = endedAt;
      this.endedBy = endedBy;
      this.searchTerms = searchTerms;
      this.variables = taskVariables!=null ? taskVariables : Map.of();
      this.taskInstanceEvents = taskInstanceEvents != null ? taskInstanceEvents : new ArrayList<>();
    }


    public void create() {
        if(processInstanceId==null) {
          throw new IllegalStateException("ProcessInstanceId cannot be null!");
        }
        if(processNumber==null) {
          throw new IllegalStateException("ProcessNumber cannot be null!");
        }
        if(applicationBase==null) {
          throw new IllegalStateException("ApplicationBase cannot be null!");
        }
        this.status = TaskInstanceStatus.CREATED;
        if(startedAt==null)
            this.startedAt = LocalDateTime.now();
        this.startedBy = Code.create("user4444create");//todo
        createTaskInstanceEvent(TaskEventType.CREATE,null);
    }


    public void claim(String note) {
        this.status = TaskInstanceStatus.ASSIGNED;
        this.assignedAt = LocalDateTime.now();
        this.assignedBy = Code.create("user45666assigne");//todo
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
        this.endedBy = Code.create("user789complete");//todo
        createTaskInstanceEvent(TaskEventType.COMPLETE,null);
    }


    private void createTaskInstanceEvent(TaskEventType eventType, String note) {

        if(!this.taskInstanceEvents.isEmpty())
            this.taskInstanceEvents.clear();

        this.taskInstanceEvents.add(
            TaskInstanceEvent.builder()
            .taskInstanceId(Identifier.generate())
            .taskInstanceId(this.id)
            .eventType(eventType)
            .status(this.status)
            .performedAt(LocalDateTime.now())
            .performedBy(Code.create("user1234event")) //todo
            .note(note!=null && !note.isBlank() ? note.trim() : null)
            .build());
    }


    public TaskInstance withIdentity(Code applicationBase, Identifier processInstanceId) {
        return TaskInstance.builder()
            .processNumber(this.processNumber)
            .taskKey(this.taskKey)
            .externalId(this.externalId)
            .name(this.name)
            .id(this.id)
            .startedAt(this.startedAt)
            .processInstanceId(processInstanceId)
            .applicationBase(applicationBase)
            .build();
    }
}
