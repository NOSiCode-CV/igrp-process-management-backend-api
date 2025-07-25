package cv.igrp.platform.process.management.area.application.queries;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cv.igrp.framework.core.domain.QueryHandler;
import cv.igrp.framework.stereotype.IgrpQueryHandler;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import cv.igrp.platform.process.management.area.application.dto.ProjectDTO;

@Component
public class ListProjectsQueryHandler implements QueryHandler<ListProjectsQuery, ResponseEntity<List<ProjectDTO>>>{

  private static final Logger LOGGER = LoggerFactory.getLogger(ListProjectsQueryHandler.class);


  public ListProjectsQueryHandler() {

  }

   @IgrpQueryHandler
  public ResponseEntity<List<ProjectDTO>> handle(ListProjectsQuery query) {
    // TODO: Implement the query handling logic here
    return null;
  }

}