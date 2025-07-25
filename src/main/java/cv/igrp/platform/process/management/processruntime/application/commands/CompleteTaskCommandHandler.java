package cv.igrp.platform.process.management.processruntime.application.commands;

import cv.igrp.framework.core.domain.CommandHandler;
import cv.igrp.framework.stereotype.IgrpCommandHandler;
import cv.igrp.platform.process.management.processruntime.application.dto.TaskInstanceDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CompleteTaskCommandHandler implements CommandHandler<CompleteTaskCommand, ResponseEntity<TaskInstanceDTO>> {

   private static final Logger LOGGER = LoggerFactory.getLogger(CompleteTaskCommandHandler.class);

   public CompleteTaskCommandHandler() {

   }

   @IgrpCommandHandler
   public ResponseEntity<TaskInstanceDTO> handle(CompleteTaskCommand command) {
      // TODO: Implement the command handling logic here
      return null;
   }

}
