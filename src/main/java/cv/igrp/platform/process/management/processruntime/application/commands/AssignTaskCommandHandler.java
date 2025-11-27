package cv.igrp.platform.process.management.processruntime.application.commands;

import cv.igrp.framework.core.domain.CommandHandler;
import cv.igrp.framework.stereotype.IgrpCommandHandler;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskOperationData;
import cv.igrp.platform.process.management.processruntime.domain.service.TaskInstanceService;
import cv.igrp.platform.process.management.shared.security.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


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
    LOGGER.info("User [{}] started assigning task [{}] to user [{}]", currentUser.getValue(), command.getId(), command.getAssigntaskdto().getUser());
    taskInstanceService.assignTask(
        TaskOperationData.builder()
            .currentUser(currentUser)
            .id(command.getId())
            .targetUser(command.getAssigntaskdto().getUser())
            .priority(command.getAssigntaskdto().getPriority())
            .note(command.getAssigntaskdto().getNote())
            .candidateGroup(command.getAssigntaskdto().getCandidateGroup())
            .build()
    );
    LOGGER.info("User [{}] finished assigning task [{}] to user [{}]", currentUser.getValue(), command.getId(), command.getAssigntaskdto().getUser());
    return ResponseEntity.noContent().build();
  }

}
