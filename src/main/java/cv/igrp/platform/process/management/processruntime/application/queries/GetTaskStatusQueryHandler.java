package cv.igrp.platform.process.management.processruntime.application.queries;

import cv.igrp.platform.process.management.processruntime.domain.service.ProcessInstanceService;
import cv.igrp.platform.process.management.processruntime.mappers.ProcessInstanceTaskStatusMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cv.igrp.framework.core.domain.QueryHandler;
import cv.igrp.framework.stereotype.IgrpQueryHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import cv.igrp.platform.process.management.processruntime.application.dto.ProcessInstanceTaskStatusDTO;

@Component
public class GetTaskStatusQueryHandler implements QueryHandler<GetTaskStatusQuery, ResponseEntity<List<ProcessInstanceTaskStatusDTO>>>{

  private static final Logger LOGGER = LoggerFactory.getLogger(GetTaskStatusQueryHandler.class);

  private final ProcessInstanceService processInstanceService;
  private final ProcessInstanceTaskStatusMapper mapper;

  public GetTaskStatusQueryHandler(ProcessInstanceService processInstanceService,
                                   ProcessInstanceTaskStatusMapper mapper) {
    this.processInstanceService = processInstanceService;
    this.mapper = mapper;
  }

   @IgrpQueryHandler
  public ResponseEntity<List<ProcessInstanceTaskStatusDTO>> handle(GetTaskStatusQuery query) {
    // TODO: Implement the query handling logic here
    return null;
  }

}
