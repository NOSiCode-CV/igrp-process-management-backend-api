package cv.igrp.platform.process.management.area.application.queries;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cv.igrp.framework.core.domain.QueryHandler;
import cv.igrp.framework.stereotype.IgrpQueryHandler;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import cv.igrp.platform.process.management.area.application.dto.AreaDTO;

@Component
public class GetAreaByIdQueryHandler implements QueryHandler<GetAreaByIdQuery, ResponseEntity<AreaDTO>>{

  private static final Logger LOGGER = LoggerFactory.getLogger(GetAreaByIdQueryHandler.class);


  public GetAreaByIdQueryHandler() {

  }

   @IgrpQueryHandler
  public ResponseEntity<AreaDTO> handle(GetAreaByIdQuery query) {
    // TODO: Implement the query handling logic here
    return null;
  }

}