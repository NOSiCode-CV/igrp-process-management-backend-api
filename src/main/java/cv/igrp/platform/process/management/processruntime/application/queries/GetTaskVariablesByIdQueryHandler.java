package cv.igrp.platform.process.management.processruntime.application.queries;

import cv.igrp.framework.core.domain.QueryHandler;
import cv.igrp.framework.stereotype.IgrpQueryHandler;
import cv.igrp.platform.process.management.processruntime.application.dto.TaskDataDTO;
import cv.igrp.platform.process.management.processruntime.domain.service.TaskInstanceService;
import cv.igrp.platform.process.management.processruntime.mappers.TaskInstanceMapper;
import cv.igrp.platform.process.management.shared.security.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class GetTaskVariablesByIdQueryHandler implements QueryHandler<GetTaskVariablesByIdQuery, ResponseEntity<TaskDataDTO>>{

  private static final Logger LOGGER = LoggerFactory.getLogger(GetTaskVariablesByIdQueryHandler.class);

  private final TaskInstanceService taskInstanceService;
  private final TaskInstanceMapper taskInstanceMapper;
  private final UserContext userContext;

  public GetTaskVariablesByIdQueryHandler(TaskInstanceService taskInstanceService,
                                         TaskInstanceMapper taskMapper,
                                         UserContext userContext) {
    this.taskInstanceService = taskInstanceService;
    this.taskInstanceMapper = taskMapper;
    this.userContext = userContext;
  }


  @IgrpQueryHandler
  @Transactional(readOnly = true)
  public ResponseEntity<TaskDataDTO> handle(GetTaskVariablesByIdQuery query) {

     final var taskId = UUID.fromString(query.getId());
     final var currentUser = userContext.getCurrentUser();

     LOGGER.debug("User [{}] requested variables for task [{}]", currentUser.getValue(), taskId);

     final var taskData = taskInstanceService.getTaskVariables(taskId);

     LOGGER.debug("User [{}] retrieved [{}] variables and [{}] formVars for task [{}]",
         currentUser.getValue(), taskData.getVariables().size(), taskData.getForms().size(), taskId);

     return ResponseEntity.ok(taskInstanceMapper.toTaskDataDTO(taskData));
  }

}
