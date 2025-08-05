package cv.igrp.platform.process.management.processruntime.application.queries;

import cv.igrp.framework.core.domain.QueryHandler;
import cv.igrp.framework.stereotype.IgrpQueryHandler;
import cv.igrp.platform.process.management.processruntime.application.dto.ProcessInstanceTaskStatusDTO;
import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstanceTaskStatus;
import cv.igrp.platform.process.management.processruntime.domain.service.ProcessInstanceService;
import cv.igrp.platform.process.management.processruntime.mappers.ProcessInstanceTaskStatusMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
public class GetTaskStatusQueryHandler implements QueryHandler<GetTaskStatusQuery, ResponseEntity<List<ProcessInstanceTaskStatusDTO>>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetTaskStatusQueryHandler.class);

    private final ProcessInstanceService processInstanceService;
    private final ProcessInstanceTaskStatusMapper mapper;

    public GetTaskStatusQueryHandler(ProcessInstanceService processInstanceService,
                                     ProcessInstanceTaskStatusMapper mapper) {
        this.processInstanceService = processInstanceService;
        this.mapper = mapper;
    }

    @IgrpQueryHandler
    @Transactional(readOnly = true)
    public ResponseEntity<List<ProcessInstanceTaskStatusDTO>> handle(GetTaskStatusQuery query) {
        List<ProcessInstanceTaskStatus> taskStatus = processInstanceService.getProcessInstanceTaskStatus(
            UUID.fromString(query.getId()));
        return ResponseEntity.ok(mapper.toDTO(taskStatus));
    }

}
