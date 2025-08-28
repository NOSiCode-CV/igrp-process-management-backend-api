package cv.igrp.platform.process.management.processruntime.application.queries;

import cv.igrp.framework.core.domain.QueryHandler;
import cv.igrp.framework.stereotype.IgrpQueryHandler;
import cv.igrp.platform.process.management.processruntime.application.dto.ProcessInstanceStatsDTO;
import cv.igrp.platform.process.management.processruntime.domain.service.ProcessInstanceService;
import cv.igrp.platform.process.management.processruntime.mappers.ProcessInstanceMapper;
import cv.igrp.platform.process.management.shared.security.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class GetProcessInstanceStatisticsQueryHandler implements QueryHandler<GetProcessInstanceStatisticsQuery, ResponseEntity<ProcessInstanceStatsDTO>>{

  private static final Logger LOGGER = LoggerFactory.getLogger(GetProcessInstanceStatisticsQueryHandler.class);

  private final ProcessInstanceService processInstanceService;
  private final ProcessInstanceMapper processInstanceMapper;
  private final UserContext userContext;

  public GetProcessInstanceStatisticsQueryHandler(ProcessInstanceService processInstanceService, ProcessInstanceMapper processInstanceMapper, UserContext userContext) {

    this.processInstanceService = processInstanceService;
    this.processInstanceMapper = processInstanceMapper;
    this.userContext = userContext;
  }

  @IgrpQueryHandler
  @Transactional(readOnly = true)
  public ResponseEntity<ProcessInstanceStatsDTO> handle(GetProcessInstanceStatisticsQuery query) {
    final var currentUser = userContext.getCurrentUser();

    LOGGER.debug("Process statistics requested byUser={}", currentUser.getValue());

    var statistics = processInstanceService.getProcessInstanceStatistics();

    LOGGER.debug("Process statistics computed successfully by user [{}]", currentUser.getValue());

    return ResponseEntity.ok(processInstanceMapper.toProcessInstanceStatsDto(statistics));

  }

}
