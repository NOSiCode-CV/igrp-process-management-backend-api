package cv.igrp.platform.process.management.processruntime.application.queries;

import cv.igrp.framework.core.domain.QueryHandler;
import cv.igrp.framework.stereotype.IgrpQueryHandler;
import cv.igrp.platform.process.management.processruntime.application.dto.TaskStatsDTO;
import cv.igrp.platform.process.management.processruntime.domain.service.TaskInstanceService;
import cv.igrp.platform.process.management.processruntime.mappers.TaskInstanceMapper;
import cv.igrp.platform.process.management.shared.security.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class GetMyTaskStatsQueryHandler implements QueryHandler<GetMyTaskStatsQuery, ResponseEntity<TaskStatsDTO>>{

  private static final Logger LOGGER = LoggerFactory.getLogger(GetMyTaskStatsQueryHandler.class);

  private final TaskInstanceService taskInstanceService;
  private final TaskInstanceMapper taskInstanceMapper;
  private final UserContext userContext;

  public GetMyTaskStatsQueryHandler(TaskInstanceService taskInstanceService,
                                    TaskInstanceMapper taskInstanceMapper,
                                    UserContext userContext) {

    this.taskInstanceService = taskInstanceService;
    this.taskInstanceMapper = taskInstanceMapper;
    this.userContext = userContext;
  }

  @IgrpQueryHandler
  @Transactional(readOnly = true)
  public ResponseEntity<TaskStatsDTO> handle(GetMyTaskStatsQuery query) {

    final var currentUser = userContext.getCurrentUser();

    LOGGER.debug("User [{}] requested his task instance statistics", currentUser.getValue());

    var statistics = taskInstanceService.getTaskStatisticsByUser(currentUser);

    LOGGER.debug("Task statistics computed successfully for user [{}]", currentUser.getValue());

    return ResponseEntity.ok(taskInstanceMapper.toTaskStatsDto(statistics));

  }

}
