package cv.igrp.platform.process.management.processruntime.application.queries;

import cv.igrp.framework.process.runtime.core.engine.activity.model.IGRPActivityType;
import cv.igrp.platform.process.management.processruntime.mappers.ActivityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cv.igrp.framework.core.domain.QueryHandler;
import cv.igrp.framework.stereotype.IgrpQueryHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

import cv.igrp.platform.process.management.processruntime.application.dto.ActivityProgressDTO;
import org.springframework.transaction.annotation.Transactional;
import cv.igrp.platform.process.management.processruntime.domain.service.ActivityInstanceService;


@Component
public class GetActivityProgressQueryHandler implements QueryHandler<GetActivityProgressQuery, ResponseEntity<List<ActivityProgressDTO>>> {

  private static final Logger LOGGER = LoggerFactory.getLogger(GetActivityProgressQueryHandler.class);

  private final ActivityInstanceService activityInstanceService;
  private final ActivityMapper activityMapper;

  public GetActivityProgressQueryHandler(ActivityInstanceService activityInstanceService, ActivityMapper activityMapper) {
    this.activityInstanceService = activityInstanceService;
    this.activityMapper = activityMapper;
  }

  @Transactional
  @IgrpQueryHandler
  public ResponseEntity<List<ActivityProgressDTO>> handle(GetActivityProgressQuery query) {
    IGRPActivityType type = query.getType() != null
        ? IGRPActivityType.valueOf(query.getType())
        : null;
    return ResponseEntity.ok(
        activityMapper.toProgressesDto(
            activityInstanceService.getProcessTimelineEvents(
                query.getProcessIdentifier(),
                type
            )
        )
    );

  }

}
