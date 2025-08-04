package cv.igrp.platform.process.management.processruntime.domain.models;

import cv.igrp.platform.process.management.shared.application.constants.TaskEventType;
import cv.igrp.platform.process.management.shared.application.constants.TaskInstanceStatus;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Getter
public class TaskInstance {
    private final Code applicationBase;
    private final Code processType;
    private final Identifier processInstanceId;
    private final Code processNumber;
    private final Code externalId;
    private final Identifier id;
    private final Code taskKey;
    private final Name name;
    private String searchTerms;
    private TaskInstanceStatus status;
    private LocalDateTime startedAt;
    private Code startedBy;
    private LocalDateTime assignedAt;
    private Code assignedBy;
    private LocalDateTime endedAt;
    private Code endedBy;
    private Map<String,Object> variables;
    private List<TaskInstanceEvent> taskInstanceEvents;

    @Builder
    public TaskInstance(
                        Code applicationBase,
                        Code processType,
                        Identifier processInstanceId,
                        Code processNumber,
                        Code externalId,
                        Identifier id,
                        Code taskKey,
                        Name name,
                        String searchTerms,
                        TaskInstanceStatus status,
                        LocalDateTime startedAt,
                        Code startedBy,
                        LocalDateTime assignedAt,
                        Code assignedBy,
                        LocalDateTime endedAt,
                        Code endedBy,
                        Map<String,Object> taskVariables,
                        List<TaskInstanceEvent> taskInstanceEvents)
    {
        this.applicationBase = Objects.requireNonNull(applicationBase, "Application Base cannot be null!");
        this.processType = Objects.requireNonNull(processType, "Process Type cannot be null!");
        this.processInstanceId = Objects.requireNonNull(processInstanceId, "Process Instance Id cannot be null!");
        this.processNumber = Objects.requireNonNull(processNumber, "Process Number cannot be null!");
        this.externalId = Objects.requireNonNull(externalId, "External Id cannot be null!");
        this.taskKey = Objects.requireNonNull(taskKey, "Task Key Id cannot be null!");
        this.id = id == null ? Identifier.generate() : id;
        this.name = name;
        this.searchTerms = searchTerms;
        this.status = status == null ? TaskInstanceStatus.CREATED : status;
        this.startedAt = startedAt;
        this.startedBy = startedBy;
        this.assignedAt = assignedAt;
        this.assignedBy = assignedBy;
        this.endedAt = endedAt;
        this.endedBy = endedBy;
        this.variables = taskVariables!=null ? taskVariables : Map.of();
        this.taskInstanceEvents = taskInstanceEvents;
    }


    public void create() {
        if (this.status != TaskInstanceStatus.CREATED && this.status != TaskInstanceStatus.ASSIGNED) {
            throw new IllegalStateException("The status of the task instance must be CREATED or ASSIGNED");
        }
        if (startedBy==null) {
            throw new IllegalStateException("The started by (user) of the task cannot be null!");
        }
        if(this.status != TaskInstanceStatus.CREATED) {
            if(this.assignedBy==null) {
                throw new IllegalStateException("The signed by (user) of the task cannot be null!");
            }
            this.assignedAt = LocalDateTime.now();
        }
        this.startedAt = LocalDateTime.now();
        createTaskInstanceEvent(TaskEventType.CREATE,null);
    }


    public void claim(String note) {
        this.status = TaskInstanceStatus.ASSIGNED;
        this.assignedAt = LocalDateTime.now();
        this.assignedBy = Code.create("user456");//todo
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


    public void complete(Map<String,Object> variables, String note) {
        this.status = TaskInstanceStatus.COMPLETED;
        this.endedAt = LocalDateTime.now();
        this.endedBy = Code.create("user789");//todo
        this.variables = variables!=null ? variables : Map.of();
        createTaskInstanceEvent(TaskEventType.COMPLETE,note);
    }


    private void createTaskInstanceEvent(TaskEventType eventType, String note){
        this.taskInstanceEvents = List.of(TaskInstanceEvent.builder()
            .taskInstanceId(Identifier.generate())
            .taskInstanceId(this.id)
            .eventType(eventType)
            .status(this.status)
            .performedAt(LocalDateTime.now())
            .performedBy(Code.create("user1234")) //todo
            .note(note)
            .build());
    }


}
