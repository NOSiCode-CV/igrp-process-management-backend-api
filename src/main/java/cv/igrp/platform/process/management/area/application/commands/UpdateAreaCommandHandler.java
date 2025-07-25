package cv.igrp.platform.process.management.area.application.commands;

import cv.igrp.framework.core.domain.CommandHandler;
import cv.igrp.framework.stereotype.IgrpCommandHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cv.igrp.platform.process.management.area.application.dto.AreaDTO;

@Component
public class UpdateAreaCommandHandler implements CommandHandler<UpdateAreaCommand, ResponseEntity<AreaDTO>> {

   private static final Logger LOGGER = LoggerFactory.getLogger(UpdateAreaCommandHandler.class);

   public UpdateAreaCommandHandler() {

   }

   @IgrpCommandHandler
   public ResponseEntity<AreaDTO> handle(UpdateAreaCommand command) {
      // TODO: Implement the command handling logic here
      return null;
   }

}