package cv.igrp.platform.process.management.processruntime.application.commands;

import cv.igrp.framework.core.domain.CommandHandler;
import cv.igrp.framework.stereotype.IgrpCommandHandler;
import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstance;
import cv.igrp.platform.process.management.processruntime.domain.service.ProcessInstanceService;
import cv.igrp.platform.process.management.processruntime.mappers.ProcessInstanceMapper;
import cv.igrp.platform.process.management.shared.security.util.UserContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cv.igrp.platform.process.management.processruntime.application.dto.ProcessInstanceDTO;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CreateProcessInstanceCommandHandler implements CommandHandler<CreateProcessInstanceCommand, ResponseEntity<ProcessInstanceDTO>> {

  private static final Logger LOGGER = LoggerFactory.getLogger(CreateProcessInstanceCommandHandler.class);

  private final ProcessInstanceService processInstanceService;
  private final ProcessInstanceMapper mapper;
  private final UserContext userContext;

  public CreateProcessInstanceCommandHandler(ProcessInstanceService processInstanceService,
                                             ProcessInstanceMapper mapper,
                                             UserContext userContext) {
    this.processInstanceService = processInstanceService;
    this.mapper = mapper;
    this.userContext = userContext;
  }

  @Transactional
  @IgrpCommandHandler
  public ResponseEntity<ProcessInstanceDTO> handle(CreateProcessInstanceCommand command) {
    ProcessInstance processInstance = processInstanceService.createProcessInstance(
        mapper.toModel(command.getCreateprocessrequestdto()),
        userContext.getCurrentUser().getValue()
    );
    return ResponseEntity.status(201).body(mapper.toDTO(processInstance));
  }

}
