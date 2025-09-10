package cv.igrp.platform.process.management.processdefinition.application.commands;

import cv.igrp.framework.core.domain.CommandHandler;
import cv.igrp.framework.stereotype.IgrpCommandHandler;
import cv.igrp.platform.process.management.processdefinition.application.dto.ProcessArtifactDTO;
import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessArtifact;
import cv.igrp.platform.process.management.processdefinition.domain.service.ProcessArtifactService;
import cv.igrp.platform.process.management.processdefinition.mappers.ProcessArtifactMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component
public class CreateArtifactCommandHandler implements CommandHandler<CreateArtifactCommand, ResponseEntity<ProcessArtifactDTO>> {

  private static final Logger LOGGER = LoggerFactory.getLogger(CreateArtifactCommandHandler.class);

  private final ProcessArtifactService processArtifactService;
  private final ProcessArtifactMapper mapper;

  public CreateArtifactCommandHandler(ProcessArtifactService processArtifactService,
                                      ProcessArtifactMapper mapper) {
    this.processArtifactService = processArtifactService;
    this.mapper = mapper;
  }

  @IgrpCommandHandler
  public ResponseEntity<ProcessArtifactDTO> handle(CreateArtifactCommand command) {
    ProcessArtifact processArtifact = processArtifactService.create(
        mapper.toModel(command.getProcessartifactrequestdto(), command.getId()));
    return ResponseEntity.status(201).body(mapper.toDTO(processArtifact));
  }

}
