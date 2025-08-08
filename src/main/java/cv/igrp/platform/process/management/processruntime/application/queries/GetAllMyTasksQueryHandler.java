package cv.igrp.platform.process.management.processruntime.application.queries;

import cv.igrp.framework.core.domain.QueryHandler;
import cv.igrp.framework.stereotype.IgrpQueryHandler;
import cv.igrp.platform.process.management.processruntime.application.dto.TaskInstanceListPageDTO;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstance;
import cv.igrp.platform.process.management.processruntime.domain.service.TaskInstanceService;
import cv.igrp.platform.process.management.processruntime.mappers.TaskInstanceMapper;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import cv.igrp.platform.process.management.shared.util.TempUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class GetAllMyTasksQueryHandler implements QueryHandler<GetAllMyTasksQuery, ResponseEntity<TaskInstanceListPageDTO>>{

    private static final Logger LOGGER = LoggerFactory.getLogger(GetAllMyTasksQueryHandler.class);

    private final TaskInstanceService taskInstanceService;
    private final TaskInstanceMapper taskInstanceMapper;

    public GetAllMyTasksQueryHandler(TaskInstanceService taskInstanceService,
                                         TaskInstanceMapper taskInstanceMapper) {
        this.taskInstanceService = taskInstanceService;
        this.taskInstanceMapper = taskInstanceMapper;
    }

    @IgrpQueryHandler
    @Transactional(readOnly = true)
    public ResponseEntity<TaskInstanceListPageDTO> handle(GetAllMyTasksQuery query) {
         PageableLista<TaskInstance> taskInstances =  taskInstanceService
             .getAllMyTasks( taskInstanceMapper.toFilter(
                 query,
                 TempUtil.getCurrentUser() //todo remove
             ));
         return ResponseEntity.ok(taskInstanceMapper.toTaskInstanceListPageDTO(taskInstances));
    }

}
