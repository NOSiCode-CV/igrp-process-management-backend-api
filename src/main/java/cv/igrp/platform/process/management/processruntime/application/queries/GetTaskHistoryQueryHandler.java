package cv.igrp.platform.process.management.processruntime.application.queries;

import cv.igrp.framework.core.domain.QueryHandler;
import cv.igrp.framework.stereotype.IgrpQueryHandler;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstance;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstanceFilter;
import cv.igrp.platform.process.management.processruntime.domain.service.TaskInstanceService;
import cv.igrp.platform.process.management.processruntime.mappers.TaskInstanceMapper;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;



@Component
public class GetTaskHistoryQueryHandler implements QueryHandler<GetTaskHistoryQuery, ResponseEntity<String>>{

    private static final Logger LOGGER = LoggerFactory.getLogger(GetTaskHistoryQueryHandler.class);

    private final TaskInstanceService taskInstanceService;
    private final TaskInstanceMapper taskInstanceMapper;

    public GetTaskHistoryQueryHandler(TaskInstanceService taskInstanceService,
                                   TaskInstanceMapper taskMapper) {
        this.taskInstanceService = taskInstanceService;
        this.taskInstanceMapper = taskMapper;
    }

    @IgrpQueryHandler
    public ResponseEntity<String> handle(GetTaskHistoryQuery query) {
        TaskInstanceFilter filter = TaskInstanceFilter.builder()
//            .processNumber(query.getProcessNumber() != null ? Code.create(query.getProcessNumber()) : null)
//            .processKey(query.getProcessKey() != null ? Code.create(query.getProcessKey()) : null)
//            .user(query.getUser() != null ? Code.create(query.getUser()) : null)
//            .status(query.getStatus() != null ? TaskInstanceStatus.valueOf(query.getStatus()) : null)
//            .dateFrom(DateUtil.stringToLocalDate.apply(query.getDateFrom()))
//            .dateTo(DateUtil.stringToLocalDate.apply(query.getDateTo()))
            .page(query.getPage())
            .size(query.getSize())
            .build();
        PageableLista<TaskInstance> taskInstances =  taskInstanceService.getAllTaskHistory(filter);
        return null;//ResponseEntity.ok(taskInstanceMapper.toDTO(taskInstances));
    }

}
