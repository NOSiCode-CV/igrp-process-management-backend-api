package cv.igrp.platform.process.management.shared.delegates.message.consumer;


import com.fasterxml.jackson.databind.ObjectMapper;
import cv.igrp.platform.process.management.processruntime.domain.service.ProcessInstanceService;
import cv.igrp.platform.process.management.shared.delegates.message.dto.ProcessEventDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.Map;

public abstract class AbstractProcessEventConsumer {

  protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
  protected final ProcessInstanceService processInstanceService;
  protected final ObjectMapper objectMapper;

  protected AbstractProcessEventConsumer(ProcessInstanceService processInstanceService, ObjectMapper objectMapper) {
    this.processInstanceService = processInstanceService;
    this.objectMapper = objectMapper;
  }

  protected void handleMessage(String message) {
    LOGGER.info("Received process event: {}", message);

    ProcessEventDTO event = parseMessage(message);
    if (event == null || event.getBusinessKey() == null || event.getBusinessKey().isBlank()) {
      LOGGER.warn("Invalid or incomplete event message: {}", message);
      return;
    }

    Map<String, Object> vars = event.getVariables() != null ? event.getVariables() : Collections.emptyMap();

    SecurityContextHolder.getContext().setAuthentication(
        new UsernamePasswordAuthenticationToken(
            "system-bot",
            null,
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_ACTIVITI_USER"))
        )
    );

    try {
      if (event.getMessageName() != null && !event.getMessageName().isBlank()) {
        LOGGER.info("Correlating message '{}' for businessKey '{}'", event.getMessageName(), event.getBusinessKey());
        processInstanceService.correlateMessage(event.getMessageName(), event.getBusinessKey(), vars);
      } else {
        LOGGER.info("Signaling process instance for businessKey '{}'", event.getBusinessKey());
        processInstanceService.signal(event.getBusinessKey(), vars);
      }

      LOGGER.info("Processed event successfully for businessKey: {}", event.getBusinessKey());

    } finally {
      SecurityContextHolder.clearContext();
    }
  }

  private ProcessEventDTO parseMessage(String message) {
    try {
      return objectMapper.readValue(message, ProcessEventDTO.class);
    } catch (Exception e) {
      LOGGER.error("Failed to parse ProcessEventDTO: {}", e.getMessage());
      return null;
    }
  }

}
