package cv.igrp.platform.process.management.processruntime.application.queries;

import cv.igrp.framework.core.domain.QueryHandler;
import cv.igrp.framework.stereotype.IgrpQueryHandler;
import cv.igrp.platform.process.management.processruntime.application.dto.TaskInstanceListPageDTO;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstance;
import cv.igrp.platform.process.management.processruntime.domain.service.TaskInstanceService;
import cv.igrp.platform.process.management.processruntime.mappers.TaskInstanceMapper;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import cv.igrp.platform.process.management.shared.security.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class GetAllMyTasksQueryHandler implements QueryHandler<GetAllMyTasksQuery, ResponseEntity<TaskInstanceListPageDTO>>{

  private static final Logger LOGGER = LoggerFactory.getLogger(GetAllMyTasksQueryHandler.class);

  private final TaskInstanceService taskInstanceService;
  private final TaskInstanceMapper taskInstanceMapper;
  private final UserContext userContext;

  public GetAllMyTasksQueryHandler(TaskInstanceService taskInstanceService,
                                   TaskInstanceMapper taskInstanceMapper,
                                   UserContext userContext) {
    this.taskInstanceService = taskInstanceService;
    this.taskInstanceMapper = taskInstanceMapper;
    this.userContext = userContext;
  }

  @IgrpQueryHandler
  @Transactional(readOnly = true)
  public ResponseEntity<TaskInstanceListPageDTO> handle(GetAllMyTasksQuery query) {
    final var filter = taskInstanceMapper.toFilter(query);
    final var currentUser = userContext.getCurrentUser();
    filter.setUser(currentUser);
    LOGGER.debug("User [{}] requested all his tasks with filter [{}]", currentUser.getValue(), filter);
    PageableLista<TaskInstance> taskInstances = taskInstanceService.getAllTaskInstances(filter);
    LOGGER.debug("User [{}] retrieved [{}] of his tasks", currentUser.getValue(), taskInstances.getTotalElements());
    return ResponseEntity.ok(taskInstanceMapper.toTaskInstanceListPageDTO(taskInstances));
  }

}
