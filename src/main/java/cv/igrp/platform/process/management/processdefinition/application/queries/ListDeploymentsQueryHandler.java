package cv.igrp.platform.process.management.processdefinition.application.queries;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cv.igrp.framework.core.domain.QueryHandler;
import cv.igrp.framework.stereotype.IgrpQueryHandler;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import cv.igrp.platform.process.management.processdefinition.application.dto.ProcessDeploymentListPageDTO;

@Component
public class ListDeploymentsQueryHandler implements QueryHandler<ListDeploymentsQuery, ResponseEntity<ProcessDeploymentListPageDTO>>{

  private static final Logger LOGGER = LoggerFactory.getLogger(ListDeploymentsQueryHandler.class);


  public ListDeploymentsQueryHandler() {

  }

   @IgrpQueryHandler
  public ResponseEntity<ProcessDeploymentListPageDTO> handle(ListDeploymentsQuery query) {
    // TODO: Implement the query handling logic here
    return null;
  }

}