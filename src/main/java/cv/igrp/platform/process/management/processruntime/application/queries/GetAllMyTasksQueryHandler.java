package cv.igrp.platform.process.management.processruntime.application.queries;

import cv.igrp.framework.core.domain.QueryHandler;
import cv.igrp.framework.stereotype.IgrpQueryHandler;
import cv.igrp.platform.process.management.processruntime.application.dto.TaskInstanceListaPageDTO;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstance;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstanceFilter;
import cv.igrp.platform.process.management.processruntime.domain.service.TaskInstanceService;
import cv.igrp.platform.process.management.processruntime.mappers.TaskInstanceMapper;
import cv.igrp.platform.process.management.shared.application.constants.TaskInstanceStatus;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import cv.igrp.platform.process.management.shared.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class GetAllMyTasksQueryHandler implements QueryHandler<GetAllMyTasksQuery, ResponseEntity<TaskInstanceListaPageDTO>>{

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
    public ResponseEntity<TaskInstanceListaPageDTO> handle(GetAllMyTasksQuery query) {
         TaskInstanceFilter filter = TaskInstanceFilter.builder()
             .processNumber(query.getProcessNumber() != null ? Code.create(query.getProcessNumber()) : null)
             .processKey(query.getProcessKey() != null ? Code.create(query.getProcessKey()) : null)
             //.user(Code.create(query.getUser())) todo
             .status(query.getStatus() != null ? TaskInstanceStatus.valueOf(query.getStatus()) : null)
             .dateFrom(DateUtil.stringToLocalDate.apply(query.getDateFrom()))
             .dateTo(DateUtil.stringToLocalDate.apply(query.getDateTo()))
             .page(query.getPage())
             .size(query.getSize())
             .build();
         PageableLista<TaskInstance> taskInstances =  taskInstanceService.getAllMyTasks(filter);
         return ResponseEntity.ok(taskInstanceMapper.toTaskInstanceListaPageDTO(taskInstances));
    }

}
