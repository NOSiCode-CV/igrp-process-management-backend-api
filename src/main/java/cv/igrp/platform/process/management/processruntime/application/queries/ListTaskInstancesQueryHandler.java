package cv.igrp.platform.process.management.processruntime.application.queries;

import cv.igrp.framework.core.domain.QueryHandler;
import cv.igrp.framework.stereotype.IgrpQueryHandler;
import cv.igrp.platform.process.management.processruntime.application.dto.TaskInstanceListPageDTO;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstance;
import cv.igrp.platform.process.management.processruntime.domain.service.TaskInstanceService;
import cv.igrp.platform.process.management.processruntime.mappers.TaskInstanceMapper;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ListTaskInstancesQueryHandler implements QueryHandler<ListTaskInstancesQuery, ResponseEntity<TaskInstanceListPageDTO>>{

    private static final Logger LOGGER = LoggerFactory.getLogger(ListTaskInstancesQueryHandler.class);

    private final TaskInstanceService taskInstanceService;
    private final TaskInstanceMapper taskInstanceMapper;

    public ListTaskInstancesQueryHandler(TaskInstanceService taskInstanceService,
                                     TaskInstanceMapper taskInstanceMapper) {
    this.taskInstanceService = taskInstanceService;
        this.taskInstanceMapper = taskInstanceMapper;
    }

    @IgrpQueryHandler
    @Transactional(readOnly = true)
    public ResponseEntity<TaskInstanceListPageDTO> handle(ListTaskInstancesQuery query) {
        PageableLista<TaskInstance> taskInstances =  taskInstanceService
            .getAllTaskInstances(taskInstanceMapper.toFilter(query));
        return ResponseEntity.ok(taskInstanceMapper.toTaskInstanceListPageDTO(taskInstances));
    }

}
