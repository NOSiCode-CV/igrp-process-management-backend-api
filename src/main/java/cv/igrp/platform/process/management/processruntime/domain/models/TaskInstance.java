package cv.igrp.platform.process.management.processruntime.domain.models;

import cv.igrp.platform.process.management.shared.application.constants.TaskInstanceStatus;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
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
    private final String searchTerms;
    private TaskInstanceStatus status;
    private LocalDateTime startedAt;
    private final String startedBy;
    private LocalDateTime assignedAt;
    private final String assignedBy;
    private LocalDateTime endedAt;
    private final String endedBy;
    private final Map<String,Object> taskVariables;
    private final TaskInstanceEvent taskInstanceEvent;

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
                        String startedBy,
                        LocalDateTime assignedAt,
                        String assignedBy,
                        LocalDateTime endedAt,
                        String endedBy,
                        Map<String,Object> taskVariables,
                        TaskInstanceEvent taskInstanceEvent)
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
        this.taskVariables = taskVariables!=null ? taskVariables : Map.of();
        this.taskInstanceEvent = Objects.requireNonNull(taskInstanceEvent, "TaskInstanceEvent cannot be null!");
    }


    public void create() {
        if(this.status!=TaskInstanceStatus.CREATED && this.status!=TaskInstanceStatus.ASSIGNED)
            throw new IllegalStateException("The status of the task instance must be CREATED or ASSIGNED");
        if(startedBy==null || startedBy.isEmpty())
            throw new IllegalStateException("The started by (user) of the task cannot be null!");
        this.startedAt = LocalDateTime.now();
        checkTaskInstanceEvent();
    }


    public void update() {
        if(this.status==TaskInstanceStatus.COMPLETED) {
            throw new IllegalStateException("The status of the task instance cannot be COMPLETED!");
        }
        if(this.status==TaskInstanceStatus.ASSIGNED) {
            if(assignedBy==null || assignedBy.isEmpty()) {
              throw new IllegalStateException("The assigned by (user) cannot be null!");
            }
        } else {
            ; // todo
        }
        checkTaskInstanceEvent();
    }


    public void complete() {
        if(endedBy ==null || endedBy.isEmpty()) {
            throw new IllegalStateException("The ended by by (user) cannot be null!");
        }
        if(taskVariables.isEmpty()) {
            throw new IllegalStateException("The task variables cannot not be Empty!");
        }
        this.status = TaskInstanceStatus.COMPLETED;
        this.endedAt = LocalDateTime.now();
        checkTaskInstanceEvent();
    }


    private void checkTaskInstanceEvent() {
        if (this.status != this.getTaskInstanceEvent().getStatus())
            throw new IllegalStateException(
                "The status of the task instance event must be equal to the task instance event status!");
        this.getTaskInstanceEvent().validate();
    }


}
