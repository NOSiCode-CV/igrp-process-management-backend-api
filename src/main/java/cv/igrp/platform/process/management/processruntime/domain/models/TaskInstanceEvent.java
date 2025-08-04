package cv.igrp.platform.process.management.processruntime.domain.models;

import cv.igrp.platform.process.management.shared.application.constants.TaskEventType;
import cv.igrp.platform.process.management.shared.application.constants.TaskInstanceStatus;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class TaskInstanceEvent {
    private final Identifier id;
    private final Identifier taskInstanceId;
    private final TaskEventType eventType;
    private LocalDateTime performedAt;
    private final Code performedBy;
    private final String note;
    private final TaskInstanceStatus status;


    @Builder
    public TaskInstanceEvent(
                Identifier id,
                Identifier taskInstanceId,
                TaskEventType eventType,
                LocalDateTime performedAt,
                Code performedBy,
                String note,
                TaskInstanceStatus status
    ) {
        this.id = id == null ? Identifier.generate() : id;
        this.taskInstanceId = taskInstanceId;
        this.eventType = Objects.requireNonNull(eventType, "EventType cannot be null");
        this.performedAt = Objects.requireNonNull(performedAt, "PerformedAt cannot be null");
        this.performedBy = Objects.requireNonNull(performedBy, "PerformedBy cannot be null");
        this.note = note!=null && !note.isBlank() ? note.trim() : null;
        this.status = Objects.requireNonNull(status, "Status cannot be null");
    }


    public void validate() {
        if(this.performedBy == null) {
            throw new IllegalStateException("The performed by (user) of the task instance event cannot be null or blank");
        }
        this.performedAt = LocalDateTime.now();
    }


}
