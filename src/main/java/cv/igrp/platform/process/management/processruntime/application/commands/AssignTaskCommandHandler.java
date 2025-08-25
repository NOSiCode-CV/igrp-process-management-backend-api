package cv.igrp.platform.process.management.processruntime.application.commands;

import cv.igrp.framework.core.domain.CommandHandler;
import cv.igrp.framework.stereotype.IgrpCommandHandler;
import cv.igrp.platform.process.management.processruntime.domain.service.TaskInstanceService;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.security.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Component
public class AssignTaskCommandHandler implements CommandHandler<AssignTaskCommand, ResponseEntity<String>> {

  private static final Logger LOGGER = LoggerFactory.getLogger(AssignTaskCommandHandler.class);

  private final TaskInstanceService taskInstanceService;
  private final UserContext userContext;

  public AssignTaskCommandHandler(TaskInstanceService taskInstanceService, UserContext userContext) {
    this.taskInstanceService = taskInstanceService;
    this.userContext = userContext;
  }

  @IgrpCommandHandler
  @Transactional
  public ResponseEntity<String> handle(AssignTaskCommand command) {

    final var currentUser = userContext.getCurrentUser();

    LOGGER.info("User [{}] started assigning task [{}] to user [{}]", currentUser.getValue(), command.getId(), command.getUser());

    taskInstanceService.assignTask(
        UUID.fromString(command.getId()),
        currentUser,
        Code.create(command.getUser()),
        command.getNote()
    );

    LOGGER.info("User [{}] finished assigning task [{}] to user [{}]", currentUser.getValue(), command.getId(), command.getUser());

    return ResponseEntity.noContent().build();

  }

}
