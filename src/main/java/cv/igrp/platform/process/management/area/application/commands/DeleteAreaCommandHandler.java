package cv.igrp.platform.process.management.area.application.commands;

import cv.igrp.framework.core.domain.CommandHandler;
import cv.igrp.framework.stereotype.IgrpCommandHandler;
import cv.igrp.platform.process.management.area.domain.service.AreaService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;


@Component
public class DeleteAreaCommandHandler implements CommandHandler<DeleteAreaCommand, ResponseEntity<String>> {

  private static final Logger LOGGER = LoggerFactory.getLogger(DeleteAreaCommandHandler.class);

  private final AreaService areaService;

  public DeleteAreaCommandHandler(AreaService areaService) {
    this.areaService = areaService;
  }

  @IgrpCommandHandler
  public ResponseEntity<String> handle(DeleteAreaCommand command) {
    areaService.deleteArea(UUID.fromString(command.getId()));
    return ResponseEntity.status(204).build();
  }

}
