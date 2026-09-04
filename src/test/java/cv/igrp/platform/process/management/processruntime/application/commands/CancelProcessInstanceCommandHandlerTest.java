package cv.igrp.platform.process.management.processruntime.application.commands;

import cv.igrp.platform.process.management.processruntime.application.dto.CancelProcessInstanceDTO;
import cv.igrp.platform.process.management.processruntime.application.dto.ProcessInstanceDTO;
import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstance;
import cv.igrp.platform.process.management.processruntime.domain.service.ProcessInstanceService;
import cv.igrp.platform.process.management.processruntime.mappers.ProcessInstanceMapper;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.security.util.UserContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CancelProcessInstanceCommandHandlerTest {

  @Mock
  private ProcessInstanceService processInstanceService;

  @Mock
  private ProcessInstanceMapper mapper;

  @Mock
  private UserContext userContext;

  @InjectMocks
  private CancelProcessInstanceCommandHandler handler;

  @Test
  void handle_shouldCancelProcessInstanceAndReturnOkWithDto() {
    UUID id = UUID.randomUUID();
    ProcessInstance processInstance = ProcessInstance.builder()
        .id(Identifier.create(id))
        .procReleaseKey(Code.create("invoice_process"))
        .procReleaseId(Code.create("release-1"))
        .build();
    ProcessInstanceDTO dto = new ProcessInstanceDTO();
    CancelProcessInstanceCommand command = new CancelProcessInstanceCommand(
        new CancelProcessInstanceDTO("Canceled by requester"),
        id.toString()
    );

    when(userContext.getCurrentUser()).thenReturn(Code.create("demo@nosi.cv"));
    when(processInstanceService.cancelProcessInstanceById(id, "Canceled by requester", "demo@nosi.cv"))
        .thenReturn(processInstance);
    when(mapper.toDTO(processInstance)).thenReturn(dto);

    ResponseEntity<ProcessInstanceDTO> response = handler.handle(command);

    assertEquals(200, response.getStatusCode().value());
    assertSame(dto, response.getBody());
    verify(processInstanceService)
        .cancelProcessInstanceById(id, "Canceled by requester", "demo@nosi.cv");
  }

  @Test
  void handle_shouldAllowMissingCancelPayload() {
    UUID id = UUID.randomUUID();
    ProcessInstance processInstance = ProcessInstance.builder()
        .id(Identifier.create(id))
        .procReleaseKey(Code.create("invoice_process"))
        .procReleaseId(Code.create("release-1"))
        .build();
    CancelProcessInstanceCommand command = new CancelProcessInstanceCommand(null, id.toString());

    when(userContext.getCurrentUser()).thenReturn(Code.create("demo@nosi.cv"));
    when(processInstanceService.cancelProcessInstanceById(id, null, "demo@nosi.cv"))
        .thenReturn(processInstance);
    when(mapper.toDTO(processInstance)).thenReturn(new ProcessInstanceDTO());

    handler.handle(command);

    var idCaptor = ArgumentCaptor.forClass(UUID.class);
    var reasonCaptor = ArgumentCaptor.forClass(String.class);
    var userCaptor = ArgumentCaptor.forClass(String.class);
    verify(processInstanceService)
        .cancelProcessInstanceById(idCaptor.capture(), reasonCaptor.capture(), userCaptor.capture());
    assertEquals(id, idCaptor.getValue());
    assertEquals(null, reasonCaptor.getValue());
    assertEquals("demo@nosi.cv", userCaptor.getValue());
  }

}
