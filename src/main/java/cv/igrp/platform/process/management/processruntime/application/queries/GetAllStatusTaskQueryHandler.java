package cv.igrp.platform.process.management.processruntime.application.queries;

import cv.igrp.framework.core.domain.QueryHandler;
import cv.igrp.framework.stereotype.IgrpQueryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;



@Component
public class GetAllStatusTaskQueryHandler implements QueryHandler<GetAllStatusTaskQuery, ResponseEntity<String>>{

  private static final Logger LOGGER = LoggerFactory.getLogger(GetAllStatusTaskQueryHandler.class);


  public GetAllStatusTaskQueryHandler() {

  }

   @IgrpQueryHandler
  public ResponseEntity<String> handle(GetAllStatusTaskQuery query) {
    // TODO: Implement the query handling logic here
    return null;
  }

}
