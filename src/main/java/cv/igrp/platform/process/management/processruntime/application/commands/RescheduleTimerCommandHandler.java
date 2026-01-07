package cv.igrp.platform.process.management.processruntime.application.commands;

import cv.igrp.framework.core.domain.CommandHandler;
import cv.igrp.framework.stereotype.IgrpCommandHandler;
import cv.igrp.platform.process.management.processruntime.domain.service.ProcessInstanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;


@Component
public class RescheduleTimerCommandHandler implements CommandHandler<RescheduleTimerCommand, ResponseEntity<String>> {

  private static final Logger LOGGER = LoggerFactory.getLogger(RescheduleTimerCommandHandler.class);

  private final ProcessInstanceService processInstanceService;

  public RescheduleTimerCommandHandler(ProcessInstanceService processInstanceService) {
    this.processInstanceService = processInstanceService;
  }

  @IgrpCommandHandler
  public ResponseEntity<String> handle(RescheduleTimerCommand command) {
    LOGGER.debug("RescheduleTimerCommand : {}", command);

    processInstanceService.rescheduleTimerByProcessInstanceId(
        UUID.fromString(command.getId()),
        command.getTimerrescheduledto().getElementId(),
        command.getTimerrescheduledto().getSeconds()
    );

    return ResponseEntity.status(204).build();
  }

}
