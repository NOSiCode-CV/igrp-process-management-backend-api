package cv.igrp.platform.process.management.processruntime.application.commands;

import cv.igrp.framework.core.domain.CommandHandler;
import cv.igrp.framework.stereotype.IgrpCommandHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@Component
public class UnClaimTaskCommandHandler implements CommandHandler<UnClaimTaskCommand, ResponseEntity<String>> {

   private static final Logger LOGGER = LoggerFactory.getLogger(UnClaimTaskCommandHandler.class);

   public UnClaimTaskCommandHandler() {

   }

   @IgrpCommandHandler
   public ResponseEntity<String> handle(UnClaimTaskCommand command) {
      // TODO: Implement the command handling logic here
      return null;
   }

}