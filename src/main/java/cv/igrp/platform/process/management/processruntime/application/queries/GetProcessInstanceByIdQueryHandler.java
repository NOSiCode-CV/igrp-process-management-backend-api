package cv.igrp.platform.process.management.processruntime.application.queries;

import cv.igrp.framework.core.domain.QueryHandler;
import cv.igrp.framework.stereotype.IgrpQueryHandler;
import cv.igrp.platform.process.management.processruntime.application.dto.ProcessInstanceDTO;
import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstance;
import cv.igrp.platform.process.management.processruntime.domain.service.ProcessInstanceService;
import cv.igrp.platform.process.management.processruntime.mappers.ProcessInstanceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class GetProcessInstanceByIdQueryHandler implements QueryHandler<GetProcessInstanceByIdQuery, ResponseEntity<ProcessInstanceDTO>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetProcessInstanceByIdQueryHandler.class);

    private final ProcessInstanceService processInstanceService;
    private final ProcessInstanceMapper processInstanceMapper;

    public GetProcessInstanceByIdQueryHandler(ProcessInstanceService processInstanceService,
                                              ProcessInstanceMapper processInstanceMapper) {
        this.processInstanceService = processInstanceService;
        this.processInstanceMapper = processInstanceMapper;
    }

    @IgrpQueryHandler
    @Transactional(readOnly = true)
    public ResponseEntity<ProcessInstanceDTO> handle(GetProcessInstanceByIdQuery query) {
        ProcessInstance processInstance = processInstanceService.getProcessInstanceById(UUID.fromString(query.getId()));
        return ResponseEntity.ok(processInstanceMapper.toDTO(processInstance));
    }

}
