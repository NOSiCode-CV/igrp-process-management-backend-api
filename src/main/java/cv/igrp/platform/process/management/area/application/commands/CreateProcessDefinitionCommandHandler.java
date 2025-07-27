package cv.igrp.platform.process.management.area.application.commands;

import cv.igrp.framework.core.domain.CommandHandler;
import cv.igrp.framework.stereotype.IgrpCommandHandler;
import cv.igrp.platform.process.management.area.application.dto.ProcessDefinitionRequestDTO;
import cv.igrp.platform.process.management.area.domain.models.AreaProcess;
import cv.igrp.platform.process.management.area.domain.service.AreaService;
import cv.igrp.platform.process.management.area.mappers.AreaProcessMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cv.igrp.platform.process.management.area.application.dto.ProcessDefinitionDTO;

import java.util.UUID;

@Component
public class CreateProcessDefinitionCommandHandler implements CommandHandler<CreateProcessDefinitionCommand, ResponseEntity<ProcessDefinitionDTO>> {

  private static final Logger LOGGER = LoggerFactory.getLogger(CreateProcessDefinitionCommandHandler.class);

  private final AreaService areaService;
  private final AreaProcessMapper areaProcessMapper;

  public CreateProcessDefinitionCommandHandler(AreaService areaService,
                                               AreaProcessMapper areaProcessMapper) {
    this.areaService = areaService;
    this.areaProcessMapper = areaProcessMapper;
  }

  @IgrpCommandHandler
  public ResponseEntity<ProcessDefinitionDTO> handle(CreateProcessDefinitionCommand command) {
    ProcessDefinitionRequestDTO processDefinitionRequestDTO = command.getProcessdefinitionrequestdto();
    AreaProcess areaProcess = areaService.createProcessDefinition(
        UUID.fromString(command.getAreaId()),
        areaProcessMapper.toModel(processDefinitionRequestDTO)
    );
    return ResponseEntity.ok(areaProcessMapper.toDTO(areaProcess));
  }

}
