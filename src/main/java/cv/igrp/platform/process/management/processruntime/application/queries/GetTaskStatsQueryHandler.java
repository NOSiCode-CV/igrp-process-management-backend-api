package cv.igrp.platform.process.management.processruntime.application.queries;

import cv.igrp.framework.core.domain.QueryHandler;
import cv.igrp.framework.stereotype.IgrpQueryHandler;
import cv.igrp.platform.process.management.processruntime.application.dto.TaskStatsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class GetTaskStatsQueryHandler implements QueryHandler<GetTaskStatsQuery, ResponseEntity<TaskStatsDTO>>{

  private static final Logger LOGGER = LoggerFactory.getLogger(GetTaskStatsQueryHandler.class);


  public GetTaskStatsQueryHandler() {

  }

   @IgrpQueryHandler
  public ResponseEntity<TaskStatsDTO> handle(GetTaskStatsQuery query) {

    return ResponseEntity.ok(new TaskStatsDTO(101L,75L,12L,10L, 1L));
  }

}
