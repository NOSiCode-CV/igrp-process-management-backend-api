package cv.igrp.platform.process.management.processruntime.application.queries;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cv.igrp.framework.core.domain.QueryHandler;
import cv.igrp.framework.stereotype.IgrpQueryHandler;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import cv.igrp.platform.process.management.processruntime.application.dto.ProcessInstanceDTO;

@Component
public class ListProcessInstancesQueryHandler implements QueryHandler<ListProcessInstancesQuery, ResponseEntity<List<ProcessInstanceDTO>>>{

  private static final Logger LOGGER = LoggerFactory.getLogger(ListProcessInstancesQueryHandler.class);


  public ListProcessInstancesQueryHandler() {

  }

   @IgrpQueryHandler
  public ResponseEntity<List<ProcessInstanceDTO>> handle(ListProcessInstancesQuery query) {
    // TODO: Implement the query handling logic here
    return null;
  }

}