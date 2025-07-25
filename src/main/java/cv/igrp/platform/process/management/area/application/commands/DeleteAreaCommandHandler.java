package cv.igrp.platform.process.management.area.application.commands;

import cv.igrp.framework.core.domain.CommandHandler;
import cv.igrp.framework.stereotype.IgrpCommandHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@Component
public class DeleteAreaCommandHandler implements CommandHandler<DeleteAreaCommand, ResponseEntity<String>> {

   private static final Logger LOGGER = LoggerFactory.getLogger(DeleteAreaCommandHandler.class);

   public DeleteAreaCommandHandler() {

   }

   @IgrpCommandHandler
   public ResponseEntity<String> handle(DeleteAreaCommand command) {
      // TODO: Implement the command handling logic here
      return null;
   }

}