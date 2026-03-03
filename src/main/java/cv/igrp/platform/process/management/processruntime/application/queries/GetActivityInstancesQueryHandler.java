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

import cv.igrp.platform.process.management.processruntime.application.dto.ActivityDTO;
import cv.igrp.platform.process.management.processruntime.domain.service.ActivityInstanceService;

@Component
public class GetActivityInstancesQueryHandler implements QueryHandler<GetActivityInstancesQuery, ResponseEntity<List<ActivityDTO>>> {

  private static final Logger LOGGER = LoggerFactory.getLogger(GetActivityInstancesQueryHandler.class);


  private final ActivityInstanceService activityInstanceService;
  private final ActivityMapper activityMapper;

  public GetActivityInstancesQueryHandler(ActivityInstanceService activityInstanceService, ActivityMapper activityMapper) {
    this.activityInstanceService = activityInstanceService;
    this.activityMapper = activityMapper;
  }

  @IgrpQueryHandler
  public ResponseEntity<List<ActivityDTO>> handle(GetActivityInstancesQuery query) {
    IGRPActivityType type = query.getType() != null
        ? IGRPActivityType.valueOf(query.getType())
        : null;
    return ResponseEntity.ok(
        activityMapper.toInstancesDto(
          activityInstanceService.getActiveActivityInstances(
              query.getProcessIdentifier(),
              type
          )
        )
    );

  }

}
