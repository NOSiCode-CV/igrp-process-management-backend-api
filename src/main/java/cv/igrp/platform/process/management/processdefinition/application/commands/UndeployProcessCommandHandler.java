package cv.igrp.platform.process.management.processdefinition.application.commands;

import cv.igrp.framework.core.domain.CommandHandler;
import cv.igrp.framework.stereotype.IgrpCommandHandler;
import cv.igrp.platform.process.management.processdefinition.domain.service.ProcessDeploymentService;
import cv.igrp.platform.process.management.processdefinition.mappers.ProcessDeploymentMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component
public class UndeployProcessCommandHandler implements CommandHandler<UndeployProcessCommand, ResponseEntity<String>> {

  private static final Logger LOGGER = LoggerFactory.getLogger(UndeployProcessCommandHandler.class);

  private final ProcessDeploymentService processDeploymentService;
  private final ProcessDeploymentMapper mapper;

  public UndeployProcessCommandHandler(ProcessDeploymentService processDeploymentService,
                                       ProcessDeploymentMapper mapper) {
    this.processDeploymentService = processDeploymentService;
    this.mapper = mapper;
  }

  @IgrpCommandHandler
  public ResponseEntity<String> handle(UndeployProcessCommand command) {
    processDeploymentService.undeployProcess(command.getDeploymentId());
    return ResponseEntity.status(204).build();
  }

}
