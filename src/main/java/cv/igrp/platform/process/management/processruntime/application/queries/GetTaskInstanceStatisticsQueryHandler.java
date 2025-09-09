package cv.igrp.platform.process.management.processruntime.application.queries;

import cv.igrp.framework.core.domain.QueryHandler;
import cv.igrp.framework.stereotype.IgrpQueryHandler;
import cv.igrp.platform.process.management.processruntime.application.dto.TaskInstanceStatsDTO;
import cv.igrp.platform.process.management.processruntime.domain.service.TaskInstanceService;
import cv.igrp.platform.process.management.processruntime.mappers.TaskInstanceMapper;
import cv.igrp.platform.process.management.shared.security.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class GetTaskInstanceStatisticsQueryHandler implements QueryHandler<GetTaskInstanceStatisticsQuery, ResponseEntity<TaskInstanceStatsDTO>>{

  private static final Logger LOGGER = LoggerFactory.getLogger(GetTaskInstanceStatisticsQueryHandler.class);

  private final TaskInstanceService taskInstanceService;
  private final TaskInstanceMapper taskInstanceMapper;
  private final UserContext userContext;

  public GetTaskInstanceStatisticsQueryHandler(TaskInstanceService taskInstanceService, TaskInstanceMapper taskInstanceMapper, UserContext userContext) {

    this.taskInstanceService = taskInstanceService;
    this.taskInstanceMapper = taskInstanceMapper;
    this.userContext = userContext;
  }


  @IgrpQueryHandler
  @Transactional(readOnly = true)
  public ResponseEntity<TaskInstanceStatsDTO> handle(GetTaskInstanceStatisticsQuery query) {

    final var currentUser = userContext.getCurrentUser();

    LOGGER.debug("Task statistics requested [scope=global, byUser={}]", currentUser.getValue());

    var statistics = taskInstanceService.getGlobalTaskStatistics();

    LOGGER.debug("Global task statistics computed successfully by user [{}]", currentUser.getValue());

    return ResponseEntity.ok(taskInstanceMapper.toTaskInstanceStatsDto(statistics));

  }

}
