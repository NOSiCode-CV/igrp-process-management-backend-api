package cv.igrp.platform.process.management.processruntime.application.queries;

import cv.igrp.framework.core.domain.QueryHandler;
import cv.igrp.framework.stereotype.IgrpQueryHandler;
import cv.igrp.platform.process.management.processruntime.application.dto.TaskStatsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class GetMyTaskStatsQueryHandler implements QueryHandler<GetMyTaskStatsQuery, ResponseEntity<TaskStatsDTO>>{

  private static final Logger LOGGER = LoggerFactory.getLogger(GetMyTaskStatsQueryHandler.class);


  public GetMyTaskStatsQueryHandler() {

  }

   @IgrpQueryHandler
  public ResponseEntity<TaskStatsDTO> handle(GetMyTaskStatsQuery query) {
    // TODO: Implement the query handling logic here
     return ResponseEntity.ok(new TaskStatsDTO(20L,15L,3L,2L));
  }

}
