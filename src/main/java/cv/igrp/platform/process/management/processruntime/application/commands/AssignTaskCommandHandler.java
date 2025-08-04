package cv.igrp.platform.process.management.processruntime.application.commands;

import cv.igrp.framework.core.domain.CommandHandler;
import cv.igrp.framework.stereotype.IgrpCommandHandler;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstance;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstanceEvent;
import cv.igrp.platform.process.management.processruntime.domain.service.TaskInstanceService;
import cv.igrp.platform.process.management.shared.application.constants.TaskEventType;
import cv.igrp.platform.process.management.shared.application.constants.TaskInstanceStatus;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Component
public class AssignTaskCommandHandler implements CommandHandler<AssignTaskCommand, ResponseEntity<String>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AssignTaskCommandHandler.class);

    private final TaskInstanceService taskInstanceService;

    public AssignTaskCommandHandler(TaskInstanceService taskInstanceService) {
    this.taskInstanceService = taskInstanceService;
    }

    @IgrpCommandHandler
    @Transactional
    public ResponseEntity<String> handle(AssignTaskCommand command) {

        var taskInstanceEvent = TaskInstanceEvent.builder()
            .taskInstanceId(Identifier.generate())
            .eventType(TaskEventType.ASSIGN)
            .status(TaskInstanceStatus.ASSIGNED)
            .performedAt(LocalDateTime.now())
            .performedBy(command.getUser_perform())
            .note(command.getNote())
            .build();

        var taskInstanceReq = TaskInstance.builder()
            .id(Identifier.create(command.getId()))
            .assignedBy(command.getUser_assigned())
            .assignedAt(LocalDateTime.now())
            .status(TaskInstanceStatus.ASSIGNED)
            .taskInstanceEvent(taskInstanceEvent)
            .build();

        taskInstanceService.assignTask(taskInstanceReq);
        return ResponseEntity.noContent().build();
    }

}
