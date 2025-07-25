package cv.igrp.platform.process.management.processruntime.application.commands;

import cv.igrp.framework.core.domain.CommandHandler;
import cv.igrp.framework.stereotype.IgrpCommandHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@Component
public class ClaimTaskCommandHandler implements CommandHandler<ClaimTaskCommand, ResponseEntity<String>> {

   private static final Logger LOGGER = LoggerFactory.getLogger(ClaimTaskCommandHandler.class);

   public ClaimTaskCommandHandler() {

   }

   @IgrpCommandHandler
   public ResponseEntity<String> handle(ClaimTaskCommand command) {
      // TODO: Implement the command handling logic here
      return null;
   }

}