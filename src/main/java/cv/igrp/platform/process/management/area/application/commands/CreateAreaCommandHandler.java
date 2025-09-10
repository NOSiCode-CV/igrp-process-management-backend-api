package cv.igrp.platform.process.management.area.application.commands;

import cv.igrp.framework.core.domain.CommandHandler;
import cv.igrp.framework.stereotype.IgrpCommandHandler;
import cv.igrp.platform.process.management.area.domain.models.Area;
import cv.igrp.platform.process.management.area.domain.service.AreaService;
import cv.igrp.platform.process.management.area.mappers.AreaMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cv.igrp.platform.process.management.area.application.dto.AreaDTO;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CreateAreaCommandHandler implements CommandHandler<CreateAreaCommand, ResponseEntity<AreaDTO>> {

  private static final Logger LOGGER = LoggerFactory.getLogger(CreateAreaCommandHandler.class);

  private final AreaService areaService;
  private final AreaMapper areaMapper;

  public CreateAreaCommandHandler(AreaService areaService,
                                  AreaMapper areaMapper) {
    this.areaService = areaService;
    this.areaMapper = areaMapper;
  }

  @IgrpCommandHandler
  @Transactional
  public ResponseEntity<AreaDTO> handle(CreateAreaCommand command) {
    Area area = areaService.createArea(areaMapper.toModel(command.getArearequestdto()));
    return ResponseEntity.status(201).body(areaMapper.toDTO(area));
  }

}
