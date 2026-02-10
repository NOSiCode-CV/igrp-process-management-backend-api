package cv.igrp.platform.process.management.processdefinition.application.queries;

import cv.igrp.platform.process.management.processdefinition.domain.service.TaskPriorityService;
import cv.igrp.platform.process.management.processdefinition.mappers.TaskPriorityMapper;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cv.igrp.framework.core.domain.QueryHandler;
import cv.igrp.framework.stereotype.IgrpQueryHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

import cv.igrp.platform.process.management.processdefinition.application.dto.TaskPriorityDTO;

@Component
public class GetPrioritiesByProcessDefinitionKeyQueryHandler implements QueryHandler<GetPrioritiesByProcessDefinitionKeyQuery, ResponseEntity<List<TaskPriorityDTO>>> {

  private static final Logger LOGGER = LoggerFactory.getLogger(GetPrioritiesByProcessDefinitionKeyQueryHandler.class);

  private final TaskPriorityService taskPriorityService;
  private final TaskPriorityMapper mapper;

  public GetPrioritiesByProcessDefinitionKeyQueryHandler(TaskPriorityService taskPriorityService,
                                                         TaskPriorityMapper mapper) {
    this.taskPriorityService = taskPriorityService;
    this.mapper = mapper;
  }

  @IgrpQueryHandler
  public ResponseEntity<List<TaskPriorityDTO>> handle(GetPrioritiesByProcessDefinitionKeyQuery query) {
    return ResponseEntity.ok(
        mapper.toDTO(
            taskPriorityService.getPrioritiesByProcessDefinitionKey(
                Code.create(query.getProcessKey())
            )
        )
    );
  }

}
