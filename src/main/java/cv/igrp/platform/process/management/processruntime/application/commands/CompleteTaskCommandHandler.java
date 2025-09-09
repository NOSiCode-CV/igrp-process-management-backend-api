package cv.igrp.platform.process.management.processruntime.application.commands;

import cv.igrp.framework.core.domain.CommandHandler;
import cv.igrp.framework.stereotype.IgrpCommandHandler;
import cv.igrp.platform.process.management.processruntime.application.dto.TaskInstanceDTO;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskOperationData;
import cv.igrp.platform.process.management.processruntime.domain.service.TaskInstanceService;
import cv.igrp.platform.process.management.processruntime.mappers.TaskInstanceMapper;
import cv.igrp.platform.process.management.shared.security.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Component
public class CompleteTaskCommandHandler implements CommandHandler<CompleteTaskCommand, ResponseEntity<TaskInstanceDTO>> {

   private static final Logger LOGGER = LoggerFactory.getLogger(CompleteTaskCommandHandler.class);

  private final TaskInstanceService taskInstanceService;
  private final TaskInstanceMapper taskInstanceMapper;
  private final UserContext userContext;

  public CompleteTaskCommandHandler(TaskInstanceService taskInstanceService,
                                    TaskInstanceMapper taskInstanceMapper, UserContext userContext) {
    this.taskInstanceService = taskInstanceService;
    this.taskInstanceMapper = taskInstanceMapper;
    this.userContext = userContext;
  }

  @IgrpCommandHandler
  @Transactional
  public ResponseEntity<TaskInstanceDTO> handle(CompleteTaskCommand command) {

    final var currentUser = userContext.getCurrentUser();
    LOGGER.info("User [{}] started completing task [{}]", currentUser.getValue(), command.getId());

    final var forms = new HashMap<String,Object>();
    if(command.getTaskdatadto().getForms()!=null)
      command.getTaskdatadto().getForms().forEach( f -> forms.put(f.getName(), f.getValue()));

    final var variables = new HashMap<String,Object>();
    if(command.getTaskdatadto().getVariables()!=null)
      command.getTaskdatadto().getVariables().forEach( v -> variables.put(v.getName(), v.getValue()));

    final var taskInstanceResp =  taskInstanceService.completeTask(
        TaskOperationData.builder()
            .currentUser(currentUser)
            .id(command.getId())
            .variables(variables)
            .forms(forms)
            .build()
    );

    LOGGER.info("User [{}] finished completing task [{}]", currentUser.getValue(), command.getId());
    return ResponseEntity.ok(taskInstanceMapper.toTaskInstanceDTO(taskInstanceResp));
  }

}
