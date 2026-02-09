package cv.igrp.platform.process.management.processdefinition.application.commands;

import cv.igrp.framework.core.domain.CommandHandler;
import cv.igrp.framework.stereotype.IgrpCommandHandler;
import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessDeployment;
import cv.igrp.platform.process.management.processdefinition.domain.service.ProcessDeploymentService;
import cv.igrp.platform.process.management.processdefinition.mappers.ProcessDeploymentMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cv.igrp.platform.process.management.processdefinition.application.dto.ProcessDeploymentDTO;

@Component
public class DeployProcessCommandHandler implements CommandHandler<DeployProcessCommand, ResponseEntity<ProcessDeploymentDTO>> {

  private static final Logger LOGGER = LoggerFactory.getLogger(DeployProcessCommandHandler.class);

  private final ProcessDeploymentService processDeploymentService;
  private final ProcessDeploymentMapper mapper;

  public DeployProcessCommandHandler(ProcessDeploymentService processDeploymentService,
                                     ProcessDeploymentMapper mapper) {
    this.processDeploymentService = processDeploymentService;
    this.mapper = mapper;
  }

  @IgrpCommandHandler
  public ResponseEntity<ProcessDeploymentDTO> handle(DeployProcessCommand command) {
    ProcessDeployment processDeployment = mapper.toModel(command.getProcessdeploymentrequestdto());
    return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(
        processDeploymentService.deployProcessAndConfigure(processDeployment)));
  }

}
