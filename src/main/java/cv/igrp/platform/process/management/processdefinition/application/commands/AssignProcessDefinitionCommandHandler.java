package cv.igrp.platform.process.management.processdefinition.application.commands;

import cv.igrp.framework.core.domain.CommandHandler;
import cv.igrp.framework.stereotype.IgrpCommandHandler;
import cv.igrp.platform.process.management.processdefinition.domain.service.ProcessDeploymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component
public class AssignProcessDefinitionCommandHandler implements CommandHandler<AssignProcessDefinitionCommand, ResponseEntity<String>> {

  private static final Logger LOGGER = LoggerFactory.getLogger(AssignProcessDefinitionCommandHandler.class);

  private final ProcessDeploymentService processDeploymentService;

  public AssignProcessDefinitionCommandHandler(ProcessDeploymentService processDeploymentService) {
    this.processDeploymentService = processDeploymentService;
  }

  @IgrpCommandHandler
  public ResponseEntity<String> handle(AssignProcessDefinitionCommand command) {
    processDeploymentService.assignProcessDefinition(
        command.getId(),
        command.getAssignprocessdto().getCandidateGroups()
    );
    return ResponseEntity.status(204).build();
  }

}
