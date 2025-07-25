package cv.igrp.platform.process.management.processruntime.application.queries;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cv.igrp.framework.core.domain.QueryHandler;
import cv.igrp.framework.stereotype.IgrpQueryHandler;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import cv.igrp.platform.process.management.processruntime.application.dto.TaskInstanceListaPageDTO;

@Component
public class ListTaskInstancesQueryHandler implements QueryHandler<ListTaskInstancesQuery, ResponseEntity<TaskInstanceListaPageDTO>>{

  private static final Logger LOGGER = LoggerFactory.getLogger(ListTaskInstancesQueryHandler.class);


  public ListTaskInstancesQueryHandler() {

  }

   @IgrpQueryHandler
  public ResponseEntity<TaskInstanceListaPageDTO> handle(ListTaskInstancesQuery query) {
    // TODO: Implement the query handling logic here
    return null;
  }

}