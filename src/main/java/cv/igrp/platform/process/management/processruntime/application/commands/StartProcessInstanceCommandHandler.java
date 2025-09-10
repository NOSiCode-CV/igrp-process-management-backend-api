package cv.igrp.platform.process.management.processruntime.application.commands;

import cv.igrp.framework.core.domain.CommandHandler;
import cv.igrp.framework.stereotype.IgrpCommandHandler;
import cv.igrp.platform.process.management.processruntime.application.dto.ProcessInstanceDTO;
import cv.igrp.platform.process.management.processruntime.domain.service.ProcessInstanceService;
import cv.igrp.platform.process.management.processruntime.mappers.ProcessInstanceMapper;
import cv.igrp.platform.process.management.shared.security.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class StartProcessInstanceCommandHandler implements CommandHandler<StartProcessInstanceCommand, ResponseEntity<ProcessInstanceDTO>> {

  private static final Logger LOGGER = LoggerFactory.getLogger(StartProcessInstanceCommandHandler.class);

  private final ProcessInstanceService processInstanceService;
  private final ProcessInstanceMapper mapper;
  private final UserContext userContext;

  public StartProcessInstanceCommandHandler(ProcessInstanceService processInstanceService,
                                            ProcessInstanceMapper mapper,
                                            UserContext userContext) {
    this.processInstanceService = processInstanceService;
    this.mapper = mapper;
    this.userContext = userContext;
  }

  @IgrpCommandHandler
  @Transactional
  public ResponseEntity<ProcessInstanceDTO> handle(StartProcessInstanceCommand command) {

    final var currentUser = userContext.getCurrentUser();

    LOGGER.info("User [{}] began starting process instance with definition id [{}]",
        currentUser.getValue(), command.getStartprocessrequestdto().getProcessDefinitionId());

    var processInstance = processInstanceService.
        startProcessInstance(mapper.toModel(command.getStartprocessrequestdto()),currentUser.getValue());

    LOGGER.info("User [{}] finished starting process successfully with definition id [{}]. Instance id [{}]",
        currentUser.getValue(), command.getStartprocessrequestdto().getProcessDefinitionId(), processInstance.getId().getValue());

    return ResponseEntity.status(201).body(mapper.toDTO(processInstance));
  }

}
