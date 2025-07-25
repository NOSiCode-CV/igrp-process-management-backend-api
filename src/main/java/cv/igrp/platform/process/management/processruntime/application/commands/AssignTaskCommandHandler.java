package cv.igrp.platform.process.management.processruntime.application.commands;

import cv.igrp.framework.core.domain.CommandHandler;
import cv.igrp.framework.stereotype.IgrpCommandHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@Component
public class AssignTaskCommandHandler implements CommandHandler<AssignTaskCommand, ResponseEntity<String>> {

   private static final Logger LOGGER = LoggerFactory.getLogger(AssignTaskCommandHandler.class);

   public AssignTaskCommandHandler() {

   }

   @IgrpCommandHandler
   public ResponseEntity<String> handle(AssignTaskCommand command) {
      // TODO: Implement the command handling logic here
      return null;
   }

}