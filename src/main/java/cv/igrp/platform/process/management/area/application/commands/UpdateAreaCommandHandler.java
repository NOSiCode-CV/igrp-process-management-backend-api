package cv.igrp.platform.process.management.area.application.commands;

import cv.igrp.framework.core.domain.CommandHandler;
import cv.igrp.framework.stereotype.IgrpCommandHandler;
import cv.igrp.platform.process.management.area.application.dto.AreaRequestDTO;
import cv.igrp.platform.process.management.area.domain.models.Area;
import cv.igrp.platform.process.management.area.domain.service.AreaService;
import cv.igrp.platform.process.management.area.mappers.AreaMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cv.igrp.platform.process.management.area.application.dto.AreaDTO;

import java.util.UUID;

@Component
public class UpdateAreaCommandHandler implements CommandHandler<UpdateAreaCommand, ResponseEntity<AreaDTO>> {

  private static final Logger LOGGER = LoggerFactory.getLogger(UpdateAreaCommandHandler.class);

  private final AreaService areaService;
  private final AreaMapper areaMapper;

  public UpdateAreaCommandHandler(AreaService areaService,
                                  AreaMapper areaMapper) {
    this.areaService = areaService;
    this.areaMapper = areaMapper;
  }

  @IgrpCommandHandler
  public ResponseEntity<AreaDTO> handle(UpdateAreaCommand command) {
    AreaRequestDTO areaRequestDTO = command.getArearequestdto();
    Area area = areaService.updateArea(
        UUID.fromString(command.getId()),
        areaMapper.toModel(areaRequestDTO)
    );
    return ResponseEntity.ok(areaMapper.toDTO(area));
  }

}
