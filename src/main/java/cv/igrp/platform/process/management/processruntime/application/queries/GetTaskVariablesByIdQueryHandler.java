package cv.igrp.platform.process.management.processruntime.application.queries;

import cv.igrp.framework.core.domain.QueryHandler;
import cv.igrp.framework.stereotype.IgrpQueryHandler;
import cv.igrp.platform.process.management.processruntime.application.dto.TaskVariableDTO;
import cv.igrp.platform.process.management.processruntime.domain.service.TaskInstanceService;
import cv.igrp.platform.process.management.processruntime.mappers.TaskInstanceMapper;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.security.util.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class GetTaskVariablesByIdQueryHandler implements QueryHandler<GetTaskVariablesByIdQuery, ResponseEntity<List<TaskVariableDTO>>>{

  private static final Logger LOGGER = LoggerFactory.getLogger(GetTaskVariablesByIdQueryHandler.class);

  private final TaskInstanceService taskInstanceService;
  private final TaskInstanceMapper taskInstanceMapper;
  private final UserContext userContext;

  public GetTaskVariablesByIdQueryHandler(TaskInstanceService taskInstanceService,
                                       TaskInstanceMapper taskInstanceMapper,
                                       UserContext userContext) {
    this.taskInstanceService = taskInstanceService;
    this.taskInstanceMapper = taskInstanceMapper;
    this.userContext = userContext;
  }


  @IgrpQueryHandler
  @Transactional(readOnly = true)
  public ResponseEntity<List<TaskVariableDTO>> handle(GetTaskVariablesByIdQuery query) {
    final var currentUser = userContext.getCurrentUser();
    LOGGER.debug("User [{}] requested variables for task [{}]", currentUser.getValue(), query.getId());
    final var taskVariables = taskInstanceService.getTaskVariables(Identifier.create(query.getId()));
    LOGGER.debug("User [{}] retrieved [{}] variables for task [{}]", currentUser.getValue(), taskVariables, query.getId());
    return ResponseEntity.ok(taskInstanceMapper.toTaskVariableListDTO(taskVariables));
  }

}
