package cv.igrp.platform.process.management.processruntime.application.queries;

import cv.igrp.framework.core.domain.QueryHandler;
import cv.igrp.framework.stereotype.IgrpQueryHandler;
import cv.igrp.platform.process.management.processruntime.application.dto.TaskVariableDTO;
import cv.igrp.platform.process.management.processruntime.domain.service.TaskInstanceService;
import cv.igrp.platform.process.management.processruntime.mappers.TaskInstanceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Component
public class GetTaskVariablesByIdQueryHandler implements QueryHandler<GetTaskVariablesByIdQuery, ResponseEntity<Set<TaskVariableDTO>>>{

    private static final Logger LOGGER = LoggerFactory.getLogger(GetTaskVariablesByIdQueryHandler.class);


    private final TaskInstanceService taskInstanceService;
    private final TaskInstanceMapper taskInstanceMapper;

    public GetTaskVariablesByIdQueryHandler(TaskInstanceService taskInstanceService,
                                            TaskInstanceMapper taskInstanceMapper) {
        this.taskInstanceService = taskInstanceService;
        this.taskInstanceMapper = taskInstanceMapper;
    }

    @IgrpQueryHandler
    @Transactional(readOnly = true)
    public ResponseEntity<Set<TaskVariableDTO>> handle(GetTaskVariablesByIdQuery query) {
        return ResponseEntity.ok(taskInstanceMapper.toTaskVariableListDTO(
            taskInstanceService.getTaskVariables(UUID.fromString(query.getId()))));
    }

}
