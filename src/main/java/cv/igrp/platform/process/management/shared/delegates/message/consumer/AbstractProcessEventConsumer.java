package cv.igrp.platform.process.management.shared.delegates.message.consumer;


import com.fasterxml.jackson.databind.ObjectMapper;
import cv.igrp.platform.process.management.processruntime.domain.service.ProcessInstanceService;
import cv.igrp.platform.process.management.shared.delegates.message.dto.ProcessEventDTO;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbstractProcessEventConsumer {

  protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
  protected final ProcessInstanceService processInstanceService;
  protected final ObjectMapper objectMapper;
  protected final JwtDecoder jwtDecoder;

  protected AbstractProcessEventConsumer(ProcessInstanceService processInstanceService, ObjectMapper objectMapper, JwtDecoder jwtDecoder) {
    this.processInstanceService = processInstanceService;
    this.objectMapper = objectMapper;
    this.jwtDecoder = jwtDecoder;
  }

  protected void handleMessage(ConsumerRecord<String, String> record) {

    var message = record.value();

    LOGGER.info("Received process event: {}", message);

    ProcessEventDTO event = parseMessage(message);
    if (event == null || event.getBusinessKey() == null || event.getBusinessKey().isBlank()) {
      LOGGER.warn("Invalid or incomplete event message: {}", message);
      return;
    }

    Map<String, Object> vars = event.getVariables() != null ? event.getVariables() : Collections.emptyMap();

    try {

      String token = null;
      Header header = record.headers().lastHeader("Authorization");
      if (header != null) {
        token = new String(header.value(), StandardCharsets.UTF_8);
      }

      Authentication auth;

      if (token != null && token.startsWith("Bearer ")) {
        String jwt = token.substring(7);
        // validate/parse JWT → build JwtAuthenticationToken
        Jwt decoded = jwtDecoder.decode(jwt);
        auth = new JwtAuthenticationToken(decoded, extractAuthorities(decoded));
      } else {
        // Use a system account when no token is provided
        auth = systemAuthentication();
      }

      SecurityContextHolder.getContext().setAuthentication(auth);

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

  protected List<SimpleGrantedAuthority> extractAuthorities(Jwt jwt) {
    // Extract authorities from JWT claims (adjust based on your JWT structure)
    Object roles = jwt.getClaim("roles");
    if (roles instanceof List<?>) {
      return ((List<?>) roles).stream()
          .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toString().toUpperCase()))
          .collect(Collectors.toList());
    }
    return Collections.singletonList(new SimpleGrantedAuthority("ROLE_ACTIVITI_USER"));
  }

  protected Authentication systemAuthentication() {
    return new UsernamePasswordAuthenticationToken(
        "system-bot",
        null,
        Collections.singletonList(new SimpleGrantedAuthority("ROLE_ACTIVITI_USER"))
    );
  }

}
