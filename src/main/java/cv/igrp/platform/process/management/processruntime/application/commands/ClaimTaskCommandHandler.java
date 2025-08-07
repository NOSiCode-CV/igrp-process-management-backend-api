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
public class ClaimTaskCommandHandler implements CommandHandler<ClaimTaskCommand, ResponseEntity<String>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClaimTaskCommandHandler.class);

    private final TaskInstanceService taskInstanceService;

    public ClaimTaskCommandHandler(TaskInstanceService taskInstanceService) {
        this.taskInstanceService = taskInstanceService;
    }

    @IgrpCommandHandler
    @Transactional
    public ResponseEntity<String> handle(ClaimTaskCommand command) {

        LOGGER.info("Start of ClaimTask id: {}",command.getId());

        taskInstanceService.claimTask(
            UUID.fromString(command.getId()),
            command.getNote()
        );

        LOGGER.info("End of ClaimTask id: {}",command.getId());

        return ResponseEntity.noContent().build();
    }

}
