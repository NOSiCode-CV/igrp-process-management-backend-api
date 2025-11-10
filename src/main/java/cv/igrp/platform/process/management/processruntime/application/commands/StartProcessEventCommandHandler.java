package cv.igrp.platform.process.management.processruntime.application.commands;

import cv.igrp.framework.core.domain.CommandHandler;
import cv.igrp.framework.stereotype.IgrpCommandHandler;
import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstance;
import cv.igrp.platform.process.management.processruntime.domain.service.ProcessInstanceService;
import cv.igrp.platform.process.management.shared.application.dto.ProcessVariableDTO;
import cv.igrp.platform.process.management.shared.application.dto.StartProcessDTO;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.stream.Collectors;

@Component
public class StartProcessEventCommandHandler implements CommandHandler<StartProcessEventCommand, ResponseEntity<String>> {

  private static final Logger LOGGER = LoggerFactory.getLogger(StartProcessEventCommandHandler.class);

  private final ProcessInstanceService processInstanceService;

  public StartProcessEventCommandHandler(ProcessInstanceService processInstanceService) {
    this.processInstanceService = processInstanceService;
  }

  @IgrpCommandHandler
  public ResponseEntity<String> handle(StartProcessEventCommand command) {

    processInstanceService.startProcessInstance(toModel(command.getStartprocessdto()), "system-bot@nosi.cv");
    LOGGER.info("Started process instance for processDefinitionId: {}", command.getStartprocessdto().getProcessDefinitionId());

    return ResponseEntity.ok("Started process instance for processDefinitionId: " + command.getStartprocessdto().getProcessDefinitionId());

  }

  private ProcessInstance toModel(StartProcessDTO dto) {
    Map<String, Object> vars = dto.getVariables().stream()
        .filter(v -> v.getName() != null)
        .collect(Collectors.toMap(
            ProcessVariableDTO::getName,
            ProcessVariableDTO::getValue,
            (a, b) -> b
        ));
    return ProcessInstance.builder()
        .procReleaseId(Code.create(dto.getProcessDefinitionId()))
        .procReleaseKey(Code.create(dto.getProcessKey()))
        .businessKey(dto.getBusinessKey() != null ? Code.create(dto.getBusinessKey()) : null)
        .applicationBase(Code.create(dto.getApplicationBase()))
        .variables(vars)
        .priority(dto.getPriority())
        .build();
  }

}
