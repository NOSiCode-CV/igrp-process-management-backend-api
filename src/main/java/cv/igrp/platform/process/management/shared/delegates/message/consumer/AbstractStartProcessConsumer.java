package cv.igrp.platform.process.management.shared.delegates.message.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstance;
import cv.igrp.platform.process.management.processruntime.domain.service.ProcessInstanceService;
import cv.igrp.platform.process.management.shared.delegates.message.dto.ProcessVariableDTO;
import cv.igrp.platform.process.management.shared.delegates.message.dto.StartProcessDTO;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbstractStartProcessConsumer {

  protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
  protected final ProcessInstanceService processInstanceService;
  protected final ObjectMapper objectMapper;

  protected AbstractStartProcessConsumer(ProcessInstanceService processInstanceService,
                                         ObjectMapper objectMapper) {
    this.processInstanceService = processInstanceService;
    this.objectMapper = objectMapper;
  }

  protected void handleMessage(String message) {
    LOGGER.info("Received message: {}", message);

    StartProcessDTO dto = parseMessage(message);
    if (dto == null) {
      LOGGER.warn("Ignored invalid message: {}", message);
      return;
    }

    SecurityContextHolder.getContext().setAuthentication(
        new UsernamePasswordAuthenticationToken(
            "system-bot",
            null,
            java.util.List.of(new SimpleGrantedAuthority("ROLE_ACTIVITI_USER"))
        )
    );

    try {
      processInstanceService.startProcessInstance(toModel(dto), "system-bot@nosi.cv");
      LOGGER.info("Started process instance for processDefinitionId: {}", dto.getProcessDefinitionId());
      LOGGER.debug("Full DTO content: {}", dto);
    } finally {
      SecurityContextHolder.clearContext();
    }
  }

  private StartProcessDTO parseMessage(String message) {
    try {
      return objectMapper.readValue(message, StartProcessDTO.class);
    } catch (Exception e) {
      LOGGER.error("Failed to parse message into StartProcessDTO: {}", e.getMessage());
      return null;
    }
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
