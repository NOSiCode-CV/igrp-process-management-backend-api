package cv.igrp.platform.process.management.processruntime.application.commands;

import cv.igrp.framework.core.domain.CommandHandler;
import cv.igrp.framework.stereotype.IgrpCommandHandler;
import cv.igrp.platform.process.management.processruntime.application.dto.ProcessInstanceDTO;
import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstance;
import cv.igrp.platform.process.management.processruntime.domain.service.ProcessInstanceService;
import cv.igrp.platform.process.management.processruntime.mappers.ProcessInstanceMapper;
import cv.igrp.platform.process.management.shared.security.util.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class CancelProcessInstanceCommandHandler implements CommandHandler<CancelProcessInstanceCommand, ResponseEntity<ProcessInstanceDTO>> {

  private static final Logger LOGGER = LoggerFactory.getLogger(CancelProcessInstanceCommandHandler.class);

  private final ProcessInstanceService processInstanceService;
  private final ProcessInstanceMapper mapper;
  private final UserContext userContext;

  public CancelProcessInstanceCommandHandler(ProcessInstanceService processInstanceService,
                                             ProcessInstanceMapper mapper,
                                             UserContext userContext) {
    this.processInstanceService = processInstanceService;
    this.mapper = mapper;
    this.userContext = userContext;
  }

  @Transactional
  @IgrpCommandHandler
  public ResponseEntity<ProcessInstanceDTO> handle(CancelProcessInstanceCommand command) {
    LOGGER.debug("CancelProcessInstanceCommand : {}", command);

    String obsCancel = command.getCancelprocessinstancedto() != null
        ? command.getCancelprocessinstancedto().getObsCancel()
        : null;

    ProcessInstance processInstance = processInstanceService.cancelProcessInstanceById(
        UUID.fromString(command.getId()),
        obsCancel,
        userContext.getCurrentUser().getValue()
    );

    return ResponseEntity.ok(mapper.toDTO(processInstance));
  }

}
