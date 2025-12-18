package cv.igrp.platform.process.management.processdefinition.application.commands;

import cv.igrp.framework.core.domain.CommandHandler;
import cv.igrp.framework.stereotype.IgrpCommandHandler;
import cv.igrp.platform.process.management.processdefinition.domain.models.ProcessArtifact;
import cv.igrp.platform.process.management.processdefinition.domain.service.ProcessArtifactService;
import cv.igrp.platform.process.management.processdefinition.mappers.ProcessArtifactMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cv.igrp.platform.process.management.processdefinition.application.dto.ProcessArtifactDTO;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ConfigureArtifactCommandHandler implements CommandHandler<ConfigureArtifactCommand, ResponseEntity<ProcessArtifactDTO>> {

  private static final Logger LOGGER = LoggerFactory.getLogger(ConfigureArtifactCommandHandler.class);

  private final ProcessArtifactService processArtifactService;
  private final ProcessArtifactMapper mapper;

  public ConfigureArtifactCommandHandler(ProcessArtifactService processArtifactService,
                                         ProcessArtifactMapper mapper) {
    this.processArtifactService = processArtifactService;
    this.mapper = mapper;
  }

  @Transactional
  @IgrpCommandHandler
  public ResponseEntity<ProcessArtifactDTO> handle(ConfigureArtifactCommand command) {
    ProcessArtifact processArtifact = processArtifactService.configureArtifact(
        mapper.toModel(
            command.getProcessartifactrequestdto(),
            command.getId(),
            command.getTaskKey()
        ));
    return ResponseEntity.ok(mapper.toDTO(processArtifact));
  }

}
