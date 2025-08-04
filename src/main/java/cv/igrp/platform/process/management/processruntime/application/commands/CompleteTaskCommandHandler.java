package cv.igrp.platform.process.management.processruntime.application.commands;

import cv.igrp.framework.core.domain.CommandHandler;
import cv.igrp.framework.stereotype.IgrpCommandHandler;
import cv.igrp.platform.process.management.processruntime.application.dto.TaskInstanceDTO;
import cv.igrp.platform.process.management.processruntime.domain.service.TaskInstanceService;
import cv.igrp.platform.process.management.processruntime.mappers.TaskInstanceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.UUID;

@Component
public class CompleteTaskCommandHandler implements CommandHandler<CompleteTaskCommand, ResponseEntity<TaskInstanceDTO>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompleteTaskCommandHandler.class);

    private final TaskInstanceService taskInstanceService;
    private final TaskInstanceMapper taskInstanceMapper;

    public CompleteTaskCommandHandler(TaskInstanceService taskInstanceService,
                                         TaskInstanceMapper taskInstanceMapper) {
        this.taskInstanceService = taskInstanceService;
        this.taskInstanceMapper = taskInstanceMapper;
    }

    @IgrpCommandHandler
    @Transactional
    public ResponseEntity<TaskInstanceDTO> handle(CompleteTaskCommand command) {

        LOGGER.info("Start of CompleteTask id: {}",command.getId());

        final var taskInstanceResp =  taskInstanceService.completeTask(
            UUID.fromString(command.getId()),
            new HashMap<>(), // todo
            command.getNote());

        LOGGER.info("End of CompleteTask id: {}",command.getId());

        return ResponseEntity.ok(taskInstanceMapper.toTaskDTO(taskInstanceResp));
    }

}
