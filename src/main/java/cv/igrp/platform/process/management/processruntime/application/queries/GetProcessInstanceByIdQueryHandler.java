package cv.igrp.platform.process.management.processruntime.application.queries;

import cv.igrp.framework.core.domain.QueryHandler;
import cv.igrp.framework.stereotype.IgrpQueryHandler;
import cv.igrp.platform.process.management.processruntime.application.dto.ProcessInstanceDTO;
import cv.igrp.platform.process.management.processruntime.domain.service.ProcessInstanceService;
import cv.igrp.platform.process.management.processruntime.mappers.ProcessInstanceMapper;
import cv.igrp.platform.process.management.shared.security.UserContext;
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
  private final UserContext userContext;

    public GetProcessInstanceByIdQueryHandler(ProcessInstanceService processInstanceService,
                                              ProcessInstanceMapper processInstanceMapper, UserContext userContext) {
        this.processInstanceService = processInstanceService;
        this.processInstanceMapper = processInstanceMapper;
      this.userContext = userContext;
    }

    @IgrpQueryHandler
    @Transactional(readOnly = true)
    public ResponseEntity<ProcessInstanceDTO> handle(GetProcessInstanceByIdQuery query) {

      final var currentUser = userContext.getCurrentUser();

      LOGGER.debug("User [{}] requested process instance by id [{}]", currentUser.getValue(), query.getId());

      var processInstance = processInstanceService.getProcessInstanceById(UUID.fromString(query.getId()));

      LOGGER.debug("Retrieved task process [{}] for user [{}]", processInstance.getId().getValue(), currentUser.getValue());

      return ResponseEntity.ok(processInstanceMapper.toDTO(processInstance));
    }

}
