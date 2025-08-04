package cv.igrp.platform.process.management.processruntime.application.commands;

import cv.igrp.framework.core.domain.CommandHandler;
import cv.igrp.framework.stereotype.IgrpCommandHandler;
import cv.igrp.platform.process.management.processruntime.application.dto.TaskInstanceDTO;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstance;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstanceEvent;
import cv.igrp.platform.process.management.processruntime.domain.service.TaskInstanceService;
import cv.igrp.platform.process.management.processruntime.mappers.TaskInstanceMapper;
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

      var taskInstanceEvent = TaskInstanceEvent.builder()
          .taskInstanceId(Identifier.generate())
          .eventType(TaskEventType.COMPLETE)
          .status(TaskInstanceStatus.COMPLETED)
          .performedAt(LocalDateTime.now())
          .performedBy(command.getUser())
          .note(command.getNote())
          .build();

       var taskInstanceReq = TaskInstance.builder()
           .id(Identifier.create(command.getId()))
           .status(TaskInstanceStatus.COMPLETED)
           .endedAt(LocalDateTime.now())
           .endedBy(command.getUser())
           .taskInstanceEvent(taskInstanceEvent)
           .build();

       final var taskInstanceResp =  taskInstanceService.completeTask(taskInstanceReq);
       return ResponseEntity.ok(taskInstanceMapper.toTaskDTO(taskInstanceResp));
   }

}
