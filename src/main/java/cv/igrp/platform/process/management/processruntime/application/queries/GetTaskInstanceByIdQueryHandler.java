package cv.igrp.platform.process.management.processruntime.application.queries;

import cv.igrp.framework.core.domain.QueryHandler;
import cv.igrp.framework.stereotype.IgrpQueryHandler;
import cv.igrp.platform.process.management.processruntime.application.dto.TaskInstanceDTO;
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
public class GetTaskInstanceByIdQueryHandler implements QueryHandler<GetTaskInstanceByIdQuery, ResponseEntity<TaskInstanceDTO>>{

  private static final Logger LOGGER = LoggerFactory.getLogger(GetTaskInstanceByIdQueryHandler.class);

  private final TaskInstanceService taskInstanceService;
  private final TaskInstanceMapper taskInstanceMapper;
  private final UserContext userContext;

  public GetTaskInstanceByIdQueryHandler(TaskInstanceService taskInstanceService,
                                         TaskInstanceMapper taskMapper,
                                         UserContext userContext) {
    this.taskInstanceService = taskInstanceService;
    this.taskInstanceMapper = taskMapper;
    this.userContext = userContext;
  }


  @IgrpQueryHandler
  @Transactional(readOnly = true)
  public ResponseEntity<TaskInstanceDTO> handle(GetTaskInstanceByIdQuery query) {

    final var currentUser = userContext.getCurrentUser();

    LOGGER.debug("User [{}] requested task instance by id [{}]", currentUser.getValue(), query.getId());

    final var taskInstance = taskInstanceService.getByIdWihEvents(UUID.fromString(query.getId()));

    LOGGER.debug("Retrieved task instance [{}] for user [{}]", taskInstance.getId(), currentUser.getValue());

    return ResponseEntity.ok(taskInstanceMapper.toTaskInstanceDTO(taskInstance));

  }

}
