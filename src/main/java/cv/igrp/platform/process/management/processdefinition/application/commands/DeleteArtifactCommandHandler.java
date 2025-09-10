package cv.igrp.platform.process.management.processdefinition.application.commands;

import cv.igrp.framework.core.domain.CommandHandler;
import cv.igrp.framework.stereotype.IgrpCommandHandler;
import cv.igrp.platform.process.management.processdefinition.domain.service.ProcessArtifactService;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component
public class DeleteArtifactCommandHandler implements CommandHandler<DeleteArtifactCommand, ResponseEntity<String>> {

  private static final Logger LOGGER = LoggerFactory.getLogger(DeleteArtifactCommandHandler.class);

  private final ProcessArtifactService processArtifactService;

  public DeleteArtifactCommandHandler(ProcessArtifactService processArtifactService) {
    this.processArtifactService = processArtifactService;
  }

  @IgrpCommandHandler
  public ResponseEntity<String> handle(DeleteArtifactCommand command) {
    processArtifactService.deleteArtifact(Identifier.create(command.getId()));
    return ResponseEntity.status(204).build();
  }

}
