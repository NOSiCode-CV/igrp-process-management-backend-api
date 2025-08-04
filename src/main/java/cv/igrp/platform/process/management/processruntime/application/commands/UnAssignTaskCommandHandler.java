package cv.igrp.platform.process.management.processruntime.application.commands;

import cv.igrp.framework.core.domain.CommandHandler;
import cv.igrp.framework.stereotype.IgrpCommandHandler;
import cv.igrp.platform.process.management.processruntime.domain.service.TaskInstanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Component
public class UnAssignTaskCommandHandler implements CommandHandler<UnAssignTaskCommand, ResponseEntity<String>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnAssignTaskCommandHandler.class);

    private final TaskInstanceService taskInstanceService;

    public UnAssignTaskCommandHandler(TaskInstanceService taskInstanceService) {
    this.taskInstanceService = taskInstanceService;
    }

    @IgrpCommandHandler
    @Transactional
    public ResponseEntity<String> handle(UnAssignTaskCommand command) {

        LOGGER.info("Start of UnAssignTask id: {}",command.getId());

        taskInstanceService.unClaimTask(
            UUID.fromString(command.getId()),
            command.getNote());

        LOGGER.info("End of UnAssignTask id: {}",command.getId());

        return ResponseEntity.noContent().build();
    }

}
