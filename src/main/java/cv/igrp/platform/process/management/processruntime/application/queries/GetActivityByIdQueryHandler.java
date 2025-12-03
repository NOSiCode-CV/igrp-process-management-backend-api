package cv.igrp.platform.process.management.processruntime.application.queries;

import cv.igrp.platform.process.management.processruntime.application.dto.ActivityDTO;
import cv.igrp.platform.process.management.processruntime.domain.service.ActivityInstanceService;
import cv.igrp.platform.process.management.processruntime.mappers.ActivityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cv.igrp.framework.core.domain.QueryHandler;
import cv.igrp.framework.stereotype.IgrpQueryHandler;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;



@Component
public class GetActivityByIdQueryHandler implements QueryHandler<GetActivityByIdQuery, ResponseEntity<ActivityDTO>>{

  private static final Logger LOGGER = LoggerFactory.getLogger(GetActivityByIdQueryHandler.class);

  private final ActivityInstanceService activityInstanceService;
  private final ActivityMapper activityMapper;

  public GetActivityByIdQueryHandler(ActivityInstanceService activityInstanceService, ActivityMapper activityMapper) {
    this.activityInstanceService = activityInstanceService;
    this.activityMapper = activityMapper;
  }

  @IgrpQueryHandler
  public ResponseEntity<ActivityDTO> handle(GetActivityByIdQuery query) {
    return ResponseEntity.ok(activityMapper.toDto(activityInstanceService.getById(query.getId())));
  }

}
