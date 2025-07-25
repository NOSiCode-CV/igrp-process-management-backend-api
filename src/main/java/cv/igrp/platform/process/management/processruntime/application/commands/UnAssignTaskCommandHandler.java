package cv.igrp.platform.process.management.processruntime.application.commands;

import cv.igrp.framework.core.domain.CommandHandler;
import cv.igrp.framework.stereotype.IgrpCommandHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@Component
public class UnAssignTaskCommandHandler implements CommandHandler<UnAssignTaskCommand, ResponseEntity<String>> {

   private static final Logger LOGGER = LoggerFactory.getLogger(UnAssignTaskCommandHandler.class);

   public UnAssignTaskCommandHandler() {

   }

   @IgrpCommandHandler
   public ResponseEntity<String> handle(UnAssignTaskCommand command) {
      // TODO: Implement the command handling logic here
      return null;
   }

}