package cv.igrp.platform.process.management.processruntime.application.queries;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cv.igrp.framework.core.domain.QueryHandler;
import cv.igrp.framework.stereotype.IgrpQueryHandler;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import cv.igrp.platform.process.management.processruntime.application.dto.TaskInstanceDTO;

@Component
public class GetTaskInstanceByIdQueryHandler implements QueryHandler<GetTaskInstanceByIdQuery, ResponseEntity<TaskInstanceDTO>>{

  private static final Logger LOGGER = LoggerFactory.getLogger(GetTaskInstanceByIdQueryHandler.class);


  public GetTaskInstanceByIdQueryHandler() {

  }

   @IgrpQueryHandler
  public ResponseEntity<TaskInstanceDTO> handle(GetTaskInstanceByIdQuery query) {
    // TODO: Implement the query handling logic here
    return null;
  }

}