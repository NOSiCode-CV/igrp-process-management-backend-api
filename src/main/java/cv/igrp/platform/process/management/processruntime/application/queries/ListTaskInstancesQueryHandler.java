package cv.igrp.platform.process.management.processruntime.application.queries;

import cv.igrp.framework.core.domain.QueryHandler;
import cv.igrp.framework.stereotype.IgrpQueryHandler;
import cv.igrp.platform.process.management.processruntime.application.dto.TaskInstanceListPageDTO;
import cv.igrp.platform.process.management.processruntime.domain.service.TaskInstanceService;
import cv.igrp.platform.process.management.processruntime.mappers.TaskInstanceMapper;
import cv.igrp.platform.process.management.shared.security.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ListTaskInstancesQueryHandler implements QueryHandler<ListTaskInstancesQuery, ResponseEntity<TaskInstanceListPageDTO>>{

  private static final Logger LOGGER = LoggerFactory.getLogger(ListTaskInstancesQueryHandler.class);

  private final TaskInstanceService taskInstanceService;
  private final TaskInstanceMapper taskInstanceMapper;
  private final UserContext userContext;

  public ListTaskInstancesQueryHandler(TaskInstanceService taskInstanceService,
                                   TaskInstanceMapper taskInstanceMapper,
                                       UserContext userContext) {
    this.taskInstanceService = taskInstanceService;
    this.taskInstanceMapper = taskInstanceMapper;
    this.userContext = userContext;
  }

  @IgrpQueryHandler
  @Transactional(readOnly = true)
  public ResponseEntity<TaskInstanceListPageDTO> handle(ListTaskInstancesQuery query) {
    final var filter = taskInstanceMapper.toFilter(query);
    final var currentUser = userContext.getCurrentUser();
    LOGGER.debug("User [{}] requested all tasks with filter [{}]", currentUser.getValue(), filter);
    final var taskInstances =  taskInstanceService.getAllTaskInstances(filter);
    LOGGER.debug("User [{}] retrieved [{}] tasks", currentUser.getValue(), taskInstances.getTotalElements());
    return ResponseEntity.ok(taskInstanceMapper.toTaskInstanceListPageDTO(taskInstances));
  }

}
