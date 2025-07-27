package cv.igrp.platform.process.management.processdefinition.application.commands;

import cv.igrp.framework.core.domain.CommandHandler;
import cv.igrp.framework.stereotype.IgrpCommandHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@Component
public class UndeployProcessCommandHandler implements CommandHandler<UndeployProcessCommand, ResponseEntity<String>> {

   private static final Logger LOGGER = LoggerFactory.getLogger(UndeployProcessCommandHandler.class);

   public UndeployProcessCommandHandler() {

   }

   @IgrpCommandHandler
   public ResponseEntity<String> handle(UndeployProcessCommand command) {
      // TODO: Implement the command handling logic here
      return null;
   }

}