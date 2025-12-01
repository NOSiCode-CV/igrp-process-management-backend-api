package cv.igrp.platform.process.management.processdefinition.application.commands;

import cv.igrp.framework.core.domain.CommandHandler;
import cv.igrp.framework.stereotype.IgrpCommandHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cv.igrp.platform.process.management.processdefinition.application.dto.ProcessArtifactDTO;

@Component
public class ConfigureArtifactCommandHandler implements CommandHandler<ConfigureArtifactCommand, ResponseEntity<ProcessArtifactDTO>> {

   private static final Logger LOGGER = LoggerFactory.getLogger(ConfigureArtifactCommandHandler.class);

   public ConfigureArtifactCommandHandler() {

   }

   @IgrpCommandHandler
   public ResponseEntity<ProcessArtifactDTO> handle(ConfigureArtifactCommand command) {
      // TODO: Implement the command handling logic here
      return null;
   }

}