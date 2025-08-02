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
import org.springframework.transaction.annotation.Transactional;


@Component
public class ClaimTaskCommandHandler implements CommandHandler<ClaimTaskCommand, ResponseEntity<String>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClaimTaskCommandHandler.class);

    private final TaskInstanceService taskInstanceService;

    public ClaimTaskCommandHandler(TaskInstanceService taskInstanceService) {
        this.taskInstanceService = taskInstanceService;
    }

    @IgrpCommandHandler
    @Transactional
    public ResponseEntity<String> handle(ClaimTaskCommand command) {
        var taskInstanceReq = TaskInstance.builder()
           .id(Identifier.create(command.getId()))
           .build();
        taskInstanceService.claimTask(taskInstanceReq);
        return ResponseEntity.noContent().build();
    }

}
