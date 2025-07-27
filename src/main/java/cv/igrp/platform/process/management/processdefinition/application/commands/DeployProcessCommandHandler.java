package cv.igrp.platform.process.management.processdefinition.application.commands;

import cv.igrp.framework.core.domain.CommandHandler;
import cv.igrp.framework.stereotype.IgrpCommandHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cv.igrp.platform.process.management.processdefinition.application.dto.ProcessDeploymentDTO;

@Component
public class DeployProcessCommandHandler implements CommandHandler<DeployProcessCommand, ResponseEntity<ProcessDeploymentDTO>> {

   private static final Logger LOGGER = LoggerFactory.getLogger(DeployProcessCommandHandler.class);

   public DeployProcessCommandHandler() {

   }

   @IgrpCommandHandler
   public ResponseEntity<ProcessDeploymentDTO> handle(DeployProcessCommand command) {
      // TODO: Implement the command handling logic here
      return null;
   }

}