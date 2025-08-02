package cv.igrp.platform.process.management.processruntime.application.commands;

import cv.igrp.framework.core.domain.CommandHandler;
import cv.igrp.framework.stereotype.IgrpCommandHandler;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstance;
import cv.igrp.platform.process.management.processruntime.domain.service.TaskInstanceService;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


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
       var taskInstanceReq = TaskInstance.builder()
           .id(Identifier.create(command.getId()))
           .user(Code.create(command.getUser()))
           .build();
       taskInstanceService.assignTask(taskInstanceReq);
       return ResponseEntity.noContent().build();
    }

}
