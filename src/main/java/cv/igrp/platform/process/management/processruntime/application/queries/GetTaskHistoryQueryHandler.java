package cv.igrp.platform.process.management.processruntime.application.queries;

import cv.igrp.framework.core.domain.QueryHandler;
import cv.igrp.framework.stereotype.IgrpQueryHandler;
import cv.igrp.platform.process.management.processruntime.application.dto.TaskInstanceHistoryDTO;
import cv.igrp.platform.process.management.processruntime.domain.service.TaskInstanceService;
import cv.igrp.platform.process.management.processruntime.mappers.TaskInstanceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class GetTaskHistoryQueryHandler implements QueryHandler<GetTaskHistoryQuery, ResponseEntity<TaskInstanceHistoryDTO>>{

    private static final Logger LOGGER = LoggerFactory.getLogger(GetTaskHistoryQueryHandler.class);


    private final TaskInstanceService taskInstanceService;
    private final TaskInstanceMapper taskInstanceMapper;

    public GetTaskHistoryQueryHandler(TaskInstanceService taskInstanceService,
                                  TaskInstanceMapper taskMapper) {
        this.taskInstanceService = taskInstanceService;
        this.taskInstanceMapper = taskMapper;
    }

    @IgrpQueryHandler
    @Transactional(readOnly = true)
    public ResponseEntity<TaskInstanceHistoryDTO> handle(GetTaskHistoryQuery query) {
//        TaskInstanceFilter filter = TaskInstanceFilter.builder() todo
//              .processNumber(query.getProcessNumber() != null ? Code.create(query.getProcessNumber()) : null)
//              .processKey(query.getProcessKey() != null ? Code.create(query.getProcessKey()) : null)
//              .user(query.getUser() != null ? Code.create(query.getUser()) : null)
//              .status(query.getStatus() != null ? TaskInstanceStatus.valueOf(query.getStatus()) : null)
//              .dateFrom(DateUtil.stringToLocalDate.apply(query.getDateFrom()))
//              .dateTo(DateUtil.stringToLocalDate.apply(query.getDateTo()))
//           .page(query.getPage())
//           .size(query.getSize())
//           .build();
//        PageableLista<TaskInstanceEvent> taskInstances =  taskInstanceService.getTaskHistory(filter);
//        return ResponseEntity.ok(taskInstanceMapper.toTaskInstanceListaPageDTO(taskInstances));
      return ResponseEntity.noContent().build();
    }

}
