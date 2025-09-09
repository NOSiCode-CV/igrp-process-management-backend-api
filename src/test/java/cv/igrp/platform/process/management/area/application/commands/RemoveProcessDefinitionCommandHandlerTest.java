package cv.igrp.platform.process.management.area.application.commands;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import cv.igrp.platform.process.management.area.domain.service.AreaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class RemoveProcessDefinitionCommandHandlerTest {

  @Mock
  private AreaService areaService;

  @InjectMocks
  private RemoveProcessDefinitionCommandHandler handler;

  @BeforeEach
  void setUp() {

  }

  @Test
  void testHandle_callsRemoveProcessDefinitionAndReturnsNoContent() {
    String areaId = UUID.randomUUID().toString();
    String processDefinitionId = UUID.randomUUID().toString();
    RemoveProcessDefinitionCommand command = new RemoveProcessDefinitionCommand(areaId, processDefinitionId);

    ResponseEntity<String> response = handler.handle(command);

    verify(areaService, times(1)).removeProcessDefinition(UUID.fromString(areaId), UUID.fromString(processDefinitionId));
    assertEquals(204, response.getStatusCode().value());
    assertNull(response.getBody());
  }

}
