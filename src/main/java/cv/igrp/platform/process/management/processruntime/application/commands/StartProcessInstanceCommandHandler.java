package cv.igrp.platform.process.management.processruntime.application.commands;

import cv.igrp.framework.core.domain.CommandHandler;
import cv.igrp.framework.stereotype.IgrpCommandHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cv.igrp.platform.process.management.processruntime.application.dto.ProcessInstanceDTO;

@Component
public class StartProcessInstanceCommandHandler implements CommandHandler<StartProcessInstanceCommand, ResponseEntity<ProcessInstanceDTO>> {

   private static final Logger LOGGER = LoggerFactory.getLogger(StartProcessInstanceCommandHandler.class);

   public StartProcessInstanceCommandHandler() {

   }

   @IgrpCommandHandler
   public ResponseEntity<ProcessInstanceDTO> handle(StartProcessInstanceCommand command) {
      // TODO: Implement the command handling logic here
      return null;
   }

}