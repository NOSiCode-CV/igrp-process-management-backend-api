package cv.igrp.platform.process.management.processruntime.application.queries;

import cv.igrp.framework.core.domain.QueryHandler;
import cv.igrp.framework.stereotype.IgrpQueryHandler;
import cv.igrp.platform.process.management.processruntime.application.dto.TaskInstanceListaPageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class GetAllMyTasksQueryHandler implements QueryHandler<GetAllMyTasksQuery, ResponseEntity<TaskInstanceListaPageDTO>>{

  private static final Logger LOGGER = LoggerFactory.getLogger(GetAllMyTasksQueryHandler.class);


  public GetAllMyTasksQueryHandler() {

  }

   @IgrpQueryHandler
  public ResponseEntity<TaskInstanceListaPageDTO> handle(GetAllMyTasksQuery query) {
    // TODO: Implement the query handling logic here
    return null;
  }

}
