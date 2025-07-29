package cv.igrp.platform.process.management.processruntime.application.queries;

import cv.igrp.framework.core.domain.QueryHandler;
import cv.igrp.framework.stereotype.IgrpQueryHandler;
import cv.igrp.platform.process.management.processruntime.application.dto.TaskInstanceDTO;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstance;
import cv.igrp.platform.process.management.processruntime.domain.service.TaskInstanceService;
import cv.igrp.platform.process.management.processruntime.mappers.TaskInstanceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class GetTaskInstanceByIdQueryHandler implements QueryHandler<GetTaskInstanceByIdQuery, ResponseEntity<TaskInstanceDTO>>{

  private static final Logger LOGGER = LoggerFactory.getLogger(GetTaskInstanceByIdQueryHandler.class);


  private final TaskInstanceService taskInstanceService;
  private final TaskInstanceMapper taskInstanceMapper;

  public GetTaskInstanceByIdQueryHandler(TaskInstanceService taskInstanceService,
                                       TaskInstanceMapper taskInstanceMapper) {
    this.taskInstanceService = taskInstanceService;
    this.taskInstanceMapper = taskInstanceMapper;
  }

   @IgrpQueryHandler
  public ResponseEntity<TaskInstanceDTO> handle(GetTaskInstanceByIdQuery query) {
     TaskInstance taskInstance =  taskInstanceService.getTaskInstanceById(UUID.fromString(query.getId()));
     return ResponseEntity.ok(taskInstanceMapper.toDTO(taskInstance));
  }

}
