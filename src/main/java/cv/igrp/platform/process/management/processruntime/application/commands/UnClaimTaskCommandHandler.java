package cv.igrp.platform.process.management.processruntime.application.commands;

import cv.igrp.framework.core.domain.CommandHandler;
import cv.igrp.framework.stereotype.IgrpCommandHandler;
import cv.igrp.platform.process.management.processruntime.domain.service.TaskInstanceService;
import cv.igrp.platform.process.management.processruntime.mappers.TaskInstanceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


@Component
public class UnClaimTaskCommandHandler implements CommandHandler<UnClaimTaskCommand, ResponseEntity<String>> {

   private static final Logger LOGGER = LoggerFactory.getLogger(UnClaimTaskCommandHandler.class);

  private final TaskInstanceService taskInstanceService;
  private final TaskInstanceMapper taskInstanceMapper;

  public UnClaimTaskCommandHandler(TaskInstanceService taskInstanceService,
                                         TaskInstanceMapper taskMapper) {
    this.taskInstanceService = taskInstanceService;
    this.taskInstanceMapper = taskMapper;
  }

   @IgrpCommandHandler
   public ResponseEntity<String> handle(UnClaimTaskCommand command) {
//     final var taskInstance =  taskInstanceService.getUnClaimTaskById(UUID.fromString(command.getId()));
//     return ResponseEntity.ok(taskInstanceMapper.toTaskDTO(taskInstance));
     return null;
   }

}
