package cv.igrp.platform.process.management.processruntime.application.commands;

import cv.igrp.framework.core.domain.CommandHandler;
import cv.igrp.framework.stereotype.IgrpCommandHandler;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstance;
import cv.igrp.platform.process.management.processruntime.domain.service.TaskInstanceService;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


@Component
public class UnAssignTaskCommandHandler implements CommandHandler<UnAssignTaskCommand, ResponseEntity<String>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnAssignTaskCommandHandler.class);

    private final TaskInstanceService taskInstanceService;

    public UnAssignTaskCommandHandler(TaskInstanceService taskInstanceService) {
    this.taskInstanceService = taskInstanceService;
    }

    @IgrpCommandHandler
    public ResponseEntity<String> handle(UnAssignTaskCommand command) {
        var taskInstanceReq = TaskInstance.builder()
           .id(Identifier.create(command.getId()))
           .build();
        taskInstanceService.claimTask(taskInstanceReq);
        return ResponseEntity.noContent().build();
    }

}
