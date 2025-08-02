package cv.igrp.platform.process.management.processruntime.domain.models;

import cv.igrp.platform.process.management.shared.application.constants.TaskInstanceStatus;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/*
 * used only to select data
 */
@Getter
public class TaskInstanceInfo {
    private final Code applicationBase;
    private final Code processType;
    private final Identifier processInstanceId;
    private final Code processNumber;
    private final Code externalId;
    private final Identifier id;
    private final Code taskKey;
    private final Name name;
    private final Code user;
    private final Code searchTerms;
    private final TaskInstanceStatus status;
    private final LocalDateTime startedAt;
    private final Code startedBy;
    private final LocalDateTime completedAt;
    private final Code completedBy;
    private final Map<String,Object> taskVariables;
    private final List<TaskInstanceEvent> taskInstanceEvents;

    @Builder
    public TaskInstanceInfo(
                            Code applicationBase,
                            Code processType,
                            Identifier processInstanceId,
                            Code processNumber,
                            Code externalId,
                            Identifier id,
                            Code taskKey,
                            Name name,
                            Code user,
                            Code searchTerms,
                            TaskInstanceStatus status,
                            LocalDateTime startedAt,
                            Code startedBy,
                            LocalDateTime completedAt,
                            Code completedBy,
                            Map<String, Object> taskVariables,
                            List<TaskInstanceEvent> taskInstanceEvents)
    {
        this.applicationBase = applicationBase;
        this.processType = processType;
        this.processInstanceId = processInstanceId;
        this.processNumber = processNumber;
        this.externalId = externalId;
        this.id = id;
        this.taskKey = taskKey;
        this.name = name;
        this.user = user;
        this.searchTerms = searchTerms;
        this.status = status;
        this.startedAt = startedAt;
        this.startedBy = startedBy;
        this.completedAt = completedAt;
        this.completedBy = completedBy;
        this.taskVariables = taskVariables!=null ? taskVariables : Map.of();
        this.taskInstanceEvents = taskInstanceEvents!=null ? taskInstanceEvents : List.of();
    }

}
